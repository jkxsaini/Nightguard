package com.example.nightguard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.nightguard.ui.theme.*
import androidx.compose.ui.unit.dp


@Composable
fun MapHandler(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MapWhite
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Mapbackground),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Map Placeholder",
                color = Color.White
            )
        }
    }
}