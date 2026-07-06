package com.example.nightguard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserProfileScreen(
    onBackToHome: () -> Unit,
    viewModel: UserViewModel = viewModel()
) {
    val users by viewModel.allUsers.collectAsState()

    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var emergencyName by remember { mutableStateOf("") }
    var emergencyPhone by remember { mutableStateOf("") }

    var savedMessage by remember { mutableStateOf("") }
    var selectedProfileName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF350000))
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "User Profile",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedProfileName.isNotEmpty()) {
            Text(
                text = "Ausgewählt: $selectedProfileName",
                color = Color.LightGray,
                fontSize = 16.sp
            )
        } else {
            Text(
                text = "Tippe unten auf ein Profil, um die Daten anzuzeigen.",
                color = Color.LightGray,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                savedMessage = ""
            },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
                savedMessage = ""
            },
            label = { Text("Telefonnummer") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = emergencyName,
            onValueChange = {
                emergencyName = it
                savedMessage = ""
            },
            label = { Text("Notfallkontakt Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = emergencyPhone,
            onValueChange = {
                emergencyPhone = it
                savedMessage = ""
            },
            label = { Text("Notfallkontakt Telefonnummer") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (
                    name.isNotBlank() &&
                    phoneNumber.isNotBlank() &&
                    emergencyName.isNotBlank() &&
                    emergencyPhone.isNotBlank()
                ) {
                    viewModel.saveUser(
                        name = name,
                        phoneNumber = phoneNumber,
                        emergencyContactName = emergencyName,
                        emergencyContactPhone = emergencyPhone
                    )

                    selectedProfileName = ""
                    name = ""
                    phoneNumber = ""
                    emergencyName = ""
                    emergencyPhone = ""

                    savedMessage = "Profil gespeichert!"
                } else {
                    savedMessage = "Bitte alle Felder ausfüllen."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6BC13B),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Speichern",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (savedMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = savedMessage,
                color = Color.White,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBackToHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Zurück")
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Gespeicherte Profile",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (users.isEmpty()) {
            Text(
                text = "Noch keine Profile gespeichert.",
                color = Color.LightGray,
                fontSize = 16.sp
            )
        } else {
            users.forEach { user ->
                UserProfileCard(
                    user = user,
                    onClick = {
                        name = user.name
                        phoneNumber = user.phoneNumber
                        emergencyName = user.emergencyContactName
                        emergencyPhone = user.emergencyContactPhone

                        selectedProfileName = user.name
                        savedMessage = "Profil geladen."
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun UserProfileCard(
    user: User,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF5B4B4B)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = user.name,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Telefon: ${user.phoneNumber}",
                color = Color.White,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Notfallkontakt: ${user.emergencyContactName}",
                color = Color.White,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Notfallnummer: ${user.emergencyContactPhone}",
                color = Color.White,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Antippen zum Anzeigen",
                color = Color.LightGray,
                fontSize = 13.sp
            )
        }
    }
}