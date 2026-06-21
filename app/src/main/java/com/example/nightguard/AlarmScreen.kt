package com.example.nightguard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlarmScreen(
    timeLeft: Int,               // Die dynamische Zeit aus dem ViewModel
    onCancelAlarm: () -> Unit    // Aktion für den "Ich bin sicher"-Button
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C1A1A)) // Dunkelroter Hintergrund aus eurem Design
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 1. Obere Warnmeldung
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .background(Color(0x80444444), shape = RoundedCornerShape(12.dp))
                .padding(all = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ungewöhnliche Bewegung erkannt!!",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        // 2. Mittlerer Bereich mit dem Countdown
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.wrapContentHeight()
        ) {
            Text(
                text = "COUNTDOWN",
                color = Color.LightGray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$timeLeft",
                color = Color.White,
                fontSize = 80.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sekunden bis Alarm",
                color = Color.LightGray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // 3. Untere Button-Sektion
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Grüner "ICH BIN SICHER" Button
            Button(
                onClick = onCancelAlarm, // Löst die Abbruch-Funktion aus
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "ICH BIN SICHER",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Roter "SOS SENDEN" Button
            Button(
                onClick = { /* Hier kommt später die sofortige SOS-Funktion hin */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "SOS SENDEN",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FakeCallScreenPreview() {
    FakeCallScreen(onAcceptCall = {}, onDeclineCall = {})
}