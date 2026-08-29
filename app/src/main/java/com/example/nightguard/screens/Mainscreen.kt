package com.example.nightguard.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.nightguard.location.ShakeDetector
import com.example.nightguard.location.LocationProvider
import com.example.nightguard.location.MapHandler
import com.example.nightguard.data.UnsafeArea
import com.example.nightguard.data.UnsafeAreaRepository

@Composable
fun MainScreen(
    locationUiState: Any? = null,
    onShareLocationClick: (String) -> Unit,
    onFakeCallClick: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onEmergencyShake: () -> Unit
) {
    val context = LocalContext.current

    val deepWine = Color(0xFF1B040D)
    val cardColor = Color(0xFF2C2C2C)
    val buttonColor = Color(0xFF111111)

    val secureStorage = remember { com.example.nightguard.data.SecureStorage(context) }
    var contacts by remember { mutableStateOf(secureStorage.getContacts()) }

    var expanded by remember { mutableStateOf(false) }

    var currentLatitude by remember { mutableStateOf(50.9375) }
    var currentLongitude by remember { mutableStateOf(6.9603) }
    var hasLocationPermission by remember { mutableStateOf(false) }
    val locationProvider = remember { LocationProvider(context) }

    val unsafeAreaRepository = remember { UnsafeAreaRepository(context.applicationContext) }
    var unsafeAreas by remember { mutableStateOf<List<UnsafeArea>>(emptyList()) }
    var firebaseError by remember { mutableStateOf<String?>(null) }
    var pendingUnsafeArea by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var selectedUnsafeRadius by remember { mutableFloatStateOf(120f) }
    var unsafeAreaMessage by remember { mutableStateOf("") }

    DisposableEffect(unsafeAreaRepository) {
        val registration = unsafeAreaRepository.listenToUnsafeAreas(
            onAreasChanged = { areas ->
                unsafeAreas = areas
                firebaseError = null
            },
            onError = { message ->
                firebaseError = message
            }
        )
        onDispose {
            registration?.remove()
        }
    }

    fun loadCurrentLocation() {
        locationProvider.requestCurrentLocation(
            onLocationReceived = { location ->
                currentLatitude = location.latitude
                currentLongitude = location.longitude
            },
            onError = {
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasLocationPermission = isGranted
        if (isGranted) {
            loadCurrentLocation()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            hasLocationPermission = true
            loadCurrentLocation()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    DisposableEffect(key1 = Unit) {
        val shakeDetector = ShakeDetector(context)
        shakeDetector.startListening {
            onEmergencyShake()
        }
        onDispose {
            shakeDetector.stopListening()
        }
    }

    pendingUnsafeArea?.let { point ->
        AlertDialog(
            onDismissRequest = {
                pendingUnsafeArea = null
                unsafeAreaMessage = ""
            },
            title = { Text("Unsicheren Bereich markieren") },
            text = {
                Column {
                    Text("Diese Position in Firebase speichern?")
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "${"%.5f".format(point.first)}, ${"%.5f".format(point.second)}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(18.dp))
                    Text("Radius: ${selectedUnsafeRadius.toInt()} m")
                    Slider(
                        value = selectedUnsafeRadius,
                        onValueChange = { selectedUnsafeRadius = it },
                        valueRange = 50f..500f
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = unsafeAreaMessage,
                        onValueChange = { text ->
                            unsafeAreaMessage = text.take(300)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nachricht (optional)") },
                        placeholder = { Text("z. B. schlecht beleuchtet oder unangenehme Situation") },
                        minLines = 2,
                        maxLines = 4,
                        supportingText = {
                            Text("${unsafeAreaMessage.length}/300")
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        unsafeAreaRepository.addUnsafeArea(
                            latitude = point.first,
                            longitude = point.second,
                            radiusMeters = selectedUnsafeRadius.toDouble(),
                            message = unsafeAreaMessage,
                            onSuccess = {
                                pendingUnsafeArea = null
                                unsafeAreaMessage = ""
                                firebaseError = null
                                Toast.makeText(
                                    context,
                                    "Unsicherer Bereich gespeichert",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onError = { message ->
                                firebaseError = message
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                ) {
                    Text("Speichern")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        pendingUnsafeArea = null
                        unsafeAreaMessage = ""
                    }
                ) {
                    Text("Abbrechen")
                }
            }
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.Black,
                contentColor = Color.White
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onFakeCallClick,
                    icon = { Icon(Icons.Filled.Call, contentDescription = "Fake Call") },
                    label = { Text("Fake Call") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = true,
                    onClick = { /* Bleibt hier */ },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToProfile,
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profil") },
                    label = { Text("Profil") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                MapHandler(
                    modifier = Modifier.fillMaxSize(),
                    latitude = currentLatitude,
                    longitude = currentLongitude,
                    zoom = 15f,
                    markerTitle = "Dein Standort",
                    unsafeAreas = unsafeAreas,
                    onUnsafeAreaLongPress = { latitude, longitude ->
                        selectedUnsafeRadius = 120f
                        unsafeAreaMessage = ""
                        pendingUnsafeArea = latitude to longitude
                    }
                )

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(12.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.72f)
                ) {
                    Text(
                        text = "Karte lange drücken → unsicheren Bereich melden",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }

                firebaseError?.let { message ->
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFD32F2F).copy(alpha = 0.92f)
                    ) {
                        Text(
                            text = message,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(deepWine)
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onShareLocationClick("Anton") },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DirectionsWalk,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Schnellstart",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Campus nach Hause mit Anton",
                                color = Color.LightGray,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                    ) {
                        Text("Standort teilen mit...", color = Color.White, fontSize = 16.sp)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Aufklappen", tint = Color.White)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(cardColor)
                            .fillMaxWidth(0.85f)
                    ) {
                        contacts.forEach { contact ->
                            DropdownMenuItem(
                                text = { Text(contact, color = Color.White, fontSize = 16.sp) },
                                onClick = {
                                    expanded = false
                                    onShareLocationClick(contact)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}