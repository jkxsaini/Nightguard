package com.example.nightguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nightguard.ui.theme.NightguardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            NightguardTheme(
                darkTheme = true,
                dynamicColor = false
            ) {
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
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            "Main" -> MainScreen(modifier = Modifier) 
                            "FakeCall" -> FakeCallScreen()
                            "Alarm" -> SOSscreen(
                                onCancelClick = {
                                    currentScreen = "Main"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainActivityPreview() {
    NightguardTheme(
        darkTheme = true,
        dynamicColor = false
    ) {
        MainScreen(
            showFakeMap = true
        )
    }
}