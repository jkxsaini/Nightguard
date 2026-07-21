package com.example.nightguard.screens

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
@Composable
fun MainScreen(
    locationUiState: Any? = null,
    onShareLocationClick: (String) -> Unit,
    onFakeCallClick: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onEmergencyShake: () -> Unit // NEU: Für den Schüttel-Sensor
) {
    val context = LocalContext.current

    // Farben
    val deepWine = Color(0xFF1B040D)
    val cardColor = Color(0xFF2C2C2C)
    val buttonColor = Color(0xFF111111)

    // Holt die echten, gespeicherten Kontakte!
    val secureStorage = remember { com.example.nightguard.data.SecureStorage(context) }
    var contacts by remember { mutableStateOf(secureStorage.getContacts()) }

// FIX: Hier muss die Variable definiert werden!
    var expanded by remember { mutableStateOf(false) }

    // GPS und Sensor State (Startposition in Köln)
    var currentLatitude by remember { mutableStateOf(50.9375) }
    var currentLongitude by remember { mutableStateOf(6.9603) }
    var hasLocationPermission by remember { mutableStateOf(false) }
    val locationProvider = remember { LocationProvider(context) }

    fun loadCurrentLocation() {
        locationProvider.requestCurrentLocation(
            onLocationReceived = { location ->
                currentLatitude = location.latitude
                currentLongitude = location.longitude
            },
            onError = {
                // Fallback bleibt Köln, wenn kein Standort verfügbar ist
            }
        )
    }

    // 1. Permission Launcher für GPS
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasLocationPermission = isGranted
        if (isGranted) {
            loadCurrentLocation()
        }
    }

    // 2. Echtes GPS abfragen
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

    // 3. Shake Detector starten
    DisposableEffect(key1 = Unit) {
        val shakeDetector = ShakeDetector(context)

        shakeDetector.startListening {
            onEmergencyShake()
        }

        onDispose {
            shakeDetector.stopListening()
        }
    }

    Scaffold(
        bottomBar = {
            // Die Menüleiste (Bottom Navigation) OHNE weißen Film
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
        // Die weiße Lücke oben schließen!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            // OBERER TEIL: OpenStreetMap mit osmdroid
            MapHandler(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                latitude = currentLatitude,
                longitude = currentLongitude,
                zoom = 15.0,
                markerTitle = "Dein Standort"
            )

            // UNTERER TEIL: Das Menü (Deep Wine Hintergrund)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(deepWine)
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Schnellstart
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

                // ECHTES DROPDOWN-MENÜ
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