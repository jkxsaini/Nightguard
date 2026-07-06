package com.example.nightguard

import com.example.nightguard.ui.theme.NightguardBackground
import com.example.nightguard.ui.theme.metallicGlassButtonColors
import com.example.nightguard.ui.theme.metallicGlassBorder
import com.example.nightguard.LocationUIState.LocationUIState
import com.example.nightguard.ui.theme.NightguardTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    locationUiState: LocationUIState = LocationUIState(),
    onShareLocationClick: () -> Unit = {},
    onFakeCallClick: () -> Unit = {}
    ) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NightguardBackground)
            .defaultMinSize(
                minWidth = 180.dp,
                minHeight = 56.dp
            )
    ) {
        MapHandler(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth()
                .height(260.dp),
            locationUiState = locationUiState
        )

        Button(
            modifier = Modifier
                .align(Alignment.Center)
                .metallicGlassBorder(),
            colors = metallicGlassButtonColors(),
            contentPadding = PaddingValues(
                horizontal = 28.dp,
                vertical = 14.dp,
            ),

            onClick = onShareLocationClick
        ) {
            Text(text = "Standort teilen mit",color = Color.White)
        }
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .metallicGlassBorder(),
            colors = metallicGlassButtonColors(),
            contentPadding = PaddingValues(
                horizontal = 28.dp,
                vertical = 14.dp
            ),

        onClick = onFakeCallClick


        ) {
            Text(text = "Fake Anruf auslösen",
            color = Color.White)
        }
    }
}
@Preview(showSystemUi = true,    name = "Main Screen",)

@Composable
fun MainScreenPreview() {
    NightguardTheme(
        darkTheme = true,
        dynamicColor = false
    ) {
        MainScreen(
            onShareLocationClick = {},
            onFakeCallClick = {}
        )
    }
}