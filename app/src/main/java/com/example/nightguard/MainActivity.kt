package com.example.nightguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


import com.example.nightguard.ui.theme.NightguardTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            
            NightguardTheme(darkTheme = true, dynamicColor = false) {
                
                
                var currentScreen by remember { mutableStateOf("Main") }

                Scaffold(
                    bottomBar = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            
                            Button(onClick = { currentScreen = "Main" }) {
                                Text("Home")
                            }
                            
                            Button(onClick = { currentScreen = "FakeCall" }) {
                                Text("Fake Call")
                            }
                            Button(onClick = { currentScreen = "Alarm" }) {
                                Text("Alarm")
                            }
                        }
                    }
                ) { innerPadding ->
                    
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            "Main" -> MainScreen(modifier = Modifier) 
                            "FakeCall" -> FakeCallScreen()            
                            "Alarm" -> AlarmScreen()                  
                        }
                    }
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    NightguardTheme(
        darkTheme = true,
        dynamicColor = false
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            MainScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}