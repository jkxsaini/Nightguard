package com.example.nightguard.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MainScreen(
    locationUiState: Any? = null,
    onShareLocationClick: (String) -> Unit,
    onFakeCallClick: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    // Farben
    val deepWine = Color(0xFF1B040D)
    val cardColor = Color(0xFF2C2C2C)
    val buttonColor = Color(0xFF111111)

    // FIX 1: Variablen für das Dropdown-Menü
    var expanded by remember { mutableStateOf(false) }
    val contacts = listOf("Mama", "Papa", "Anton", "Mitbewohnerin")

    // Start-Position für die Karte (z.B. Köln)
    val startLocation = LatLng(50.9375, 6.9603)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startLocation, 15f)
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
        // FIX 2: Die weiße Lücke oben schließen!
        // Wir nehmen nur das Padding für unten, damit die Menüleiste nicht verdeckt wird.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            // OBERER TEIL: Die Google Map (geht jetzt bis ganz nach oben)
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                cameraPositionState = cameraPositionState
            )

            // UNTERER TEIL: Das Menü (Deep Wine Hintergrund)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(deepWine)
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // FIX 3: Schnellstart ist jetzt klickbar und startet das Tracking
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onShareLocationClick("Anton") }, // HIER IST DER KLICK
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Walking Icon
                        Icon(
                            imageVector = Icons.Filled.DirectionsWalk,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        // Texte in der Karte
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

                // FIX 1: ECHTES DROPDOWN-MENÜ
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

                    // Das Menü, das beim Klicken aufklappt
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(cardColor) // Passt farblich zum Dark-Theme
                            .fillMaxWidth(0.85f)
                    ) {
                        contacts.forEach { contact ->
                            DropdownMenuItem(
                                text = { Text(contact, color = Color.White, fontSize = 16.sp) },
                                onClick = {
                                    expanded = false
                                    onShareLocationClick(contact) // Startet Tracking mit gewählter Person
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}