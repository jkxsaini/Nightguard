package com.example.nightguard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nightguard.ui.theme.NightguardBackground
import com.example.nightguard.ui.theme.NightguardTheme
import com.example.nightguard.ui.theme.metallicGlassBorder
import com.example.nightguard.ui.theme.metallicGlassButtonColors
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    showFakeMap: Boolean = false
) {
    val cameraPositionState = rememberCameraPositionState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = NightguardBackground
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(NightguardBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(310.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFE8EDF3))
                ) {
                    if (showFakeMap) {
                        FakeMapPreview(
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 28.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        modifier = Modifier
                            .defaultMinSize(
                                minWidth = 180.dp,
                                minHeight = 56.dp
                            )
                            .metallicGlassBorder(),
                        colors = metallicGlassButtonColors(),
                        contentPadding = PaddingValues(
                            horizontal = 28.dp,
                            vertical = 14.dp
                        ),
                        onClick = {
                            println("Funktion Standort teilen")
                        }
                    ) {
                        Text(
                            text = "Standort teilen mit",
                            color = Color.White
                        )
                    }

                    Box(modifier = Modifier.height(315.dp)) //distanz zwischen Buttons

                    Button(
                        modifier = Modifier
                            .defaultMinSize(
                                minWidth = 180.dp,
                                minHeight = 56.dp
                            )
                            .metallicGlassBorder(),
                        colors = metallicGlassButtonColors(),
                        contentPadding = PaddingValues(
                            horizontal = 28.dp,
                            vertical = 14.dp
                        ),
                        onClick = {
                            println("Funktion Fake Anruf")
                        }
                    ) {
                        Text(
                            text = "Fake Anruf auslösen",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FakeMapPreview(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(0xFFE8EDF3))
    ) {
        Text(
            text = "Map Preview",
            color = Color.DarkGray,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    NightguardTheme(
        darkTheme = true,
        dynamicColor = false
    ) {
        MainScreen(
            showFakeMap = true
        )
    }
}