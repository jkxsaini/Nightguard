package com.example.nightguard.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nightguard.screens.*
import com.example.nightguard.location.LocationUIState

@Composable
fun NightguardNavHost(navController: NavHostController) {
    // Speichert den Namen, standardmäßig "Mama"
    var selectedContact by remember { mutableStateOf("Mama") }

    NavHost(navController = navController, startDestination = "Main") {
        composable("Main") {
            MainScreen(
                locationUiState = LocationUIState.LocationUIState(),
                onShareLocationClick = { contact ->
                    selectedContact = contact
                    navController.navigate("Tracking")
                },
                onFakeCallClick = { navController.navigate("FakeCall") },
                onNavigateToProfile = { navController.navigate("UserProfile") }
            )
        }

        composable("Tracking") {
            TrackingScreen(
                contactName = selectedContact,
                onStopTracking = { navController.popBackStack() }, // Geht zurück zum MainScreen
                onFakeCallClick = { navController.navigate("FakeCall") },
                onPoliceCallClick = {},
                onSosClick = { navController.navigate("SOS") },
                // NEU: Klick auf Icon öffnet den AlarmScreen (Countdown)
                onShakeTriggered = { navController.navigate("AlarmScreen") }
            )
        }

        composable("FakeCall") {
            FakeCallScreen(
                onAcceptCall = { navController.popBackStack() },
                onDeclineCall = { navController.popBackStack() }
            )
        }

        composable("SOS") {
            SOSScreen(
                contactName = selectedContact, // Name wird an SOS übergeben
                onFalseAlarmClick = { navController.popBackStack() }, // Geht 1 Schritt zurück (zum TrackingScreen)
                onSafeClick = { navController.popBackStack("Main", inclusive = false) }, // Geht direkt zurück zum Homescreen
                onPoliceClick = {}
            )
        }

        composable("AlarmScreen") {
            AlarmScreen(
                onFalseAlarmClick = { navController.popBackStack() },
                onSafeClick = { navController.popBackStack() },
                onPoliceClick = {},
                onTimeout = {
                    navController.popBackStack() // Schließt den Countdown-Screen
                    navController.navigate("SOS") // Öffnet sofort den SOS-Screen
                }
            )
        }

        composable("UserProfile") {
            UserProfileScreen(onBackToHome = { navController.popBackStack() })
        }
    }
}