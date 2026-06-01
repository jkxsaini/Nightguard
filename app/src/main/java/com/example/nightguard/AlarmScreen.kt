package com.example.nightguardtest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlarmScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C1A1A))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .background(Color(0x80000000), shape = RoundedCornerShape(12.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ungewöhnliche Bewegung erkannt!!",
                color = Color.White,
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 2. Countdown
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "COUNTDOWN",
                color = Color.White,
                fontSize = 28.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "15",
                color = Color.White,
                fontSize = 150.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Sekunden bis Alarm",
                color = Color.LightGray,
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Button(
                onClick = { /* Funktion */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Text(
                    text = "ICH BIN SICHER",
                    color = Color.White,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { /* Funktion */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Text(
                    text = "SOS SENDEN",
                    color = Color.White,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_8")
@Composable
fun AlarmScreenPreview() {
    AlarmScreen()
}