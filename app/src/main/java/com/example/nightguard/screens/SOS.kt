package com.example.nightguard.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SOSScreen(
    contactName: String = "Mama",
    onFalseAlarmClick: () -> Unit,
    onSafeClick: () -> Unit,
    onPoliceClick: () -> Unit
) {
    var showPinDialog by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf("") }
    val correctPin = "1234"

    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = { showPinDialog = false; pinInput = ""; pinError = false },
            title = { Text(if (pendingAction == "SAFE") "Sicherheit bestätigen" else "Fehlalarm abbrechen") },
            text = {
                Column {
                    Text("Bitte PIN eingeben:")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { pinInput = it; pinError = false },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        isError = pinError,
                        singleLine = true,
                        label = { Text("PIN") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (pinInput == correctPin) {
                        showPinDialog = false
                        if (pendingAction == "SAFE") onSafeClick()
                        if (pendingAction == "FALSE_ALARM") onFalseAlarmClick()
                        pinInput = ""
                    } else {
                        pinError = true
                    }
                }) { Text("Bestätigen") }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false; pinInput = ""; pinError = false }) {
                    Text("Abbrechen")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD32F2F)) // Alarm-Rot
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = "SOS Aktiv",
            tint = Color.White,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "SOS AUSGELÖST",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Dein Live-Standort wird an $contactName gesendet. Das Mikrofon nimmt zur Beweissicherung auf.",
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        // Aktions-Buttons
        Button(
            onClick = {
                pendingAction = "SAFE"
                showPinDialog = true
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(50)
        ) {
            Text("Ich bin in Sicherheit", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onPoliceClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(50)
        ) {
            Text("Polizei rufen (110)", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 1. NEU: Fehlalarm Button mit Umrandung (OutlinedButton)
        OutlinedButton(
            onClick = {
                pendingAction = "FALSE_ALARM"
                showPinDialog = true
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, Color.White), // Weiße Umrandung
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text("Fehlalarm abbrechen", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}