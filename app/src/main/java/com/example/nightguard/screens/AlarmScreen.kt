package com.example.nightguard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nightguard.data.SecureStorage
import kotlinx.coroutines.delay

@Composable
fun AlarmScreen(
    onFalseAlarmClick: () -> Unit,
    onSafeClick: () -> Unit,
    onPoliceClick: () -> Unit,
    onTimeout: () -> Unit
) {
    // Lokalen Speicher für die echte PIN abrufen
    val context = LocalContext.current
    val secureStorage = remember { SecureStorage(context) }
    val correctPin = remember { secureStorage.getPin() }

    // State für den 15-Sekunden-Countdown
    var timeLeft by remember { mutableStateOf(15) }

    // State für den PIN-Dialog
    var showPinDialog by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }

    // Speichert, welche Aktion nach erfolgreicher PIN-Eingabe ausgeführt werden soll
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    // Design-Farben
    val brightOrange = Color(0xFFFF6D00)
    val buttonGrey = Color(0xFF333333)
    val policeRed = Color(0xFFD32F2F)

    // Der Timer: Läuft automatisch runter
    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        } else {
            // Bei 0 wird der Notfall ausgelöst!
            onTimeout()
        }
    }

    // Der PIN-Dialog zum Abbrechen
    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = {
                showPinDialog = false
                pinInput = ""
                pinError = false
                pendingAction = null
            },
            title = { Text("Alarm abbrechen") },
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
                        // Führt die Aktion aus, die der User ursprünglich angeklickt hat
                        pendingAction?.invoke()
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
                    pendingAction = null
                }) { Text("Zurück") }
            }
        )
    }

    // Die eigentliche UI des Screens
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brightOrange)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "AUTOMATISCHER SOS-ALARM",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Wird ausgelöst in",
            color = Color.White,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        // Die große Countdown-Zahl
        Text(
            text = "$timeLeft",
            color = Color.White,
            fontSize = 140.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.weight(1f))

        // Die drei Buttons
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            // Button 1: Ich bin in Sicherheit
            Button(
                onClick = {
                    pendingAction = onSafeClick
                    showPinDialog = true
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = buttonGrey)
            ) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Ich bin in Sicherheit", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            // Button 2: Fehlalarm abbrechen
            Button(
                onClick = {
                    pendingAction = onFalseAlarmClick
                    showPinDialog = true
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = buttonGrey)
            ) {
                Icon(Icons.Filled.Close, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Fehlalarm abbrechen", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            // Button 3: Polizei Notruf (Löst SOFORT aus, keine PIN)
            Button(
                onClick = { onPoliceClick() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = policeRed)
            ) {
                Icon(Icons.Filled.LocalPolice, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Polizei Notruf (110)", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}