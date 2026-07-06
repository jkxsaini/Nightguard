package com.example.nightguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.nightguard.ui.theme.NightguardTheme
import com.example.nightguard.navigation.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NightguardTheme(darkTheme = true, dynamicColor = false) {

                val navController = rememberNavController()

                // 2. Antons Scaffold für die Menüleiste unten
                Scaffold(
                    bottomBar = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(onClick = { navigateToMain(navController) }) {
                                Text("Home")
                            }
                            Button(onClick = {navigateToFakeCall(navController) }) {
                                Text("Fake Call")
                            }
                            Button(onClick = { navigateToAlarm(navController) }) {
                                Text("Alarm")
                            }
                            Button(onClick = { navigateToSOS(navController) }){
                                Text("SOS")
                            }
                            Button(onClick = { navigateToUserProfileScreen(navController) }) {
                                Text("Profil")

                            }
                        }
                    }
                ) { innerPadding ->
                    NightguardNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}