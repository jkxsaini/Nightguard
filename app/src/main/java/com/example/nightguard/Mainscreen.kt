package com.example.nightguard

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.nightguard.ui.theme.NightguardBackground
import com.example.nightguard.ui.theme.metallicGlassButtonColors
import com.example.nightguard.ui.theme.metallicGlassBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable // Composeable ist eine einmalige Sache bei jedem Run, keine Recomposition
fun MainScreen(modifier: Modifier = Modifier) { // State Management! Remember, Calclaton : State
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NightguardBackground)
            .defaultMinSize(
                minWidth = 180.dp,
                minHeight = 56.dp
            )
    ) {
        Button(
            modifier = Modifier.align(Alignment.Center)
                .metallicGlassBorder(),
            colors = metallicGlassButtonColors(),
            contentPadding = PaddingValues(
                horizontal = 28.dp,
                vertical = 14.dp,
            ),




            onClick = { println("Funktion Standort teilen") }
        ) {
            Text(text = "Standort teilen mit")
        }
        Button(
            modifier = Modifier.align(Alignment.BottomCenter)
                .metallicGlassBorder(),
            colors = metallicGlassButtonColors(),
            contentPadding = PaddingValues(
                horizontal = 28.dp,
                vertical = 14.dp
            ),

        onClick = { println("Funktion Fake Anruf") }


        ) {
            Text(text = "Fake Anruf auslösen",
            color = Color.White)
        }
    }
}