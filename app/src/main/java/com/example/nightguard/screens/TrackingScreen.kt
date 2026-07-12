package com.example.nightguard.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TrackingScreen(
    contactName: String,
    onStopTracking: () -> Unit,
    onFakeCallClick: () -> Unit,
    onPoliceCallClick: () -> Unit,
    onSosClick: () -> Unit,
    onShakeTriggered: () -> Unit = {}
) {
    var showPinDialog by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }
    val correctPin = "1234"

    val deepWine = Color(0xFF1B040D)
    val buttonGrey = Color(0xFF333333)
    val policeRed = Color(0xFFD32F2F)
    val sosRed = Color(0xFFEF5350)

    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = {
                showPinDialog = false
                pinInput = ""
                pinError = false
            },
            title = { Text("Tracking beenden") },
            text = {
                Column {
                    Text("Bitte PIN eingeben, um fortzufahren:")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = {
                            pinInput = it
                            pinError = false
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        isError = pinError,
                        singleLine = true,
                        label = { Text("PIN") },
                        supportingText = {
                            if (pinError) {
                                Text("Falsche PIN. Bitte erneut versuchen.", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (pinInput == correctPin) {
                        showPinDialog = false
                        pinInput = ""
                        onStopTracking()
                    } else {
                        pinError = true
                    }
                }) { Text("Bestätigen") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPinDialog = false
                    pinInput = ""
                    pinError = false
                }) {
                    Text("Abbrechen")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(deepWine)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // 3. NEU: Versteckter Shortcut auf dem Icon
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "Active Tracking (Klick simuliert Crash)",
            tint = Color(0xFF4CAF50),
            modifier = Modifier
                .size(84.dp)
                .clickable { onShakeTriggered() } // Löst den Shortcut aus
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Standort wird geteilt",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "mit $contactName",
            color = Color.LightGray,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { showPinDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = buttonGrey)
        ) {
            Text(
                text = "Sicher angekommen (Beenden)",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            OutlinedButton(
                onClick = onFakeCallClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, Color.Gray),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Icon(Icons.Filled.Call, null)
                Spacer(Modifier.width(12.dp))
                Text("Fake Call auslösen", fontSize = 16.sp)
            }

            Button(
                onClick = onPoliceCallClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = policeRed)
            ) {
                Icon(Icons.Filled.LocalPolice, null)
                Spacer(Modifier.width(12.dp))
                Text("Polizei Notruf (110)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Button(
                onClick = onSosClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = sosRed)
            ) {
                Icon(Icons.Filled.Warning, null)
                Spacer(Modifier.width(12.dp))
                Text("SOS an $contactName", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}