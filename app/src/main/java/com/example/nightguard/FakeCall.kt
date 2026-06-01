package com.example.nightguard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FakeCallScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)) // Sehr dunkler Hintergrund
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "GEMINI",
                color = Color.White,
                fontSize = 48.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Light,
                letterSpacing = 4.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Eingehender Anruf...",
                color = Color.LightGray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 60.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            LargeCircularButton(
                symbol = "✖️",
                backgroundColor = Color(0xFFB00020)
            )


            LargeCircularButton(
                symbol = "📞",
                backgroundColor = Color(0xFF4CAF50)
            )
        }
    }
}

@Composable
fun LargeCircularButton(
    symbol: String,
    backgroundColor: Color
) {
    Surface(
        onClick = { /* Funktion kommt später */ },
        modifier = Modifier.size(85.dp),
        shape = CircleShape,
        color = backgroundColor,
        shadowElevation = 8.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = symbol,
                fontSize = 36.sp
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_8")
@Composable
fun FakeCallScreenPreview() {
    FakeCallScreen()
}