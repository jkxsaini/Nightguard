package com.example.nightguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import com.example.nightguard.ui.theme.NightguardTheme
import com.example.nightguard.ui.AlarmViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NightguardTheme(darkTheme = true, dynamicColor = false) {

                // 1. Dein schlaues Navigations-Gehirn
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
                            // Die Buttons nutzen jetzt deinen professionellen NavController!
                            Button(onClick = { navController.navigate("Main") }) {
                                Text("Home")
                            }
                            Button(onClick = { navController.navigate("FakeCall") }) {
                                Text("Fake Call")
                            }
                            Button(onClick = { navController.navigate("Alarm") }) {
                                Text("Alarm")
                            }
                            Button(onClick = { navController.navigate(route = "SOS") }){
                                Text("SOS")
                            }
                        }
                    }
                ) { innerPadding ->

                    // 3. Hier kommt dein NavHost rein (ersetzt euren alten "when"-Block)
                    NavHost(
                        navController = navController,
                        startDestination = "Main",
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        // --- SCREEN 1: Antons MainScreen ---
                        composable("Main") {
                            MainScreen(modifier = Modifier)
                        }

                        // --- SCREEN 2: Dein Fake Call ---
                        composable("FakeCall") {
                            FakeCallScreen(
                                onAcceptCall = { navController.popBackStack() },
                                onDeclineCall = { navController.popBackStack() }
                            )
                        }

                        // --- SCREEN 3: Dein SOS Alarm (mit dem ViewModel!) ---
                        composable("Alarm") {
                            val alarmViewModel: AlarmViewModel = viewModel()
                            val timeLeft by alarmViewModel.timeLeft.collectAsState()

                            LaunchedEffect(Unit) {
                                alarmViewModel.startCountdown()
                            }

                            AlarmScreen(
                                timeLeft = timeLeft,
                                onCancelAlarm = {
                                    alarmViewModel.cancelCountdown()
                                    navController.navigate("Main") // Sicher zurück ins Hauptmenü
                                }
                            )
                        }

                        // --- SCREEN 4: Der neue SOS Screen vom Team ---
                        composable("SOS") {
                            SOSscreen(
                                onCancelClick = {
                                    navController.navigate("Main") // Sicher zurück ins Hauptmenü
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}