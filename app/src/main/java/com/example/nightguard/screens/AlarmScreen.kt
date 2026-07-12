package com.example.nightguard.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun AlarmScreen(
    onFalseAlarmClick: () -> Unit,
    onSafeClick: () -> Unit,
    onPoliceClick: () -> Unit,
    onTimeout: () -> Unit
) {
    var timeLeft by remember { mutableStateOf(10) } // 10 Sekunden Countdown
    val deepWine = Color(0xFF1B040D)

    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        } else {
            onTimeout() // Löst die Weiterleitung zum SOS Screen aus
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(deepWine)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = "Achtung",
            tint = Color(0xFFD32F2F),
            modifier = Modifier.size(110.dp) // Groß, aber nicht zu extrem
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "$timeLeft",
            color = Color.White,
            fontSize = 100.sp, // Deutlich größer für schnelle Lesbarkeit
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = "Sekunden bis SOS",
            color = Color.LightGray,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = onFalseAlarmClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, Color.White), // Weiße Umrandung wie besprochen
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text("Fehlalarm abbrechen", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSafeClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333))
        ) {
            Text("Ich bin in Sicherheit", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onPoliceClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
            Icon(Icons.Filled.LocalPolice, null)
            Spacer(Modifier.width(8.dp))
            Text("Polizei rufen (110)", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}