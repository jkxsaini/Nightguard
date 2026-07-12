package com.example.nightguard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FakeCallScreen(
    onAcceptCall: () -> Unit,
    onDeclineCall: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E)) // Dunkler Hintergrund wie beim echten Anruf
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Anrufer Info (Oben)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(64.dp))
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.Gray, CircleShape)
                    .padding(24.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text("Mama", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
            Text("Mobiltelefon", color = Color.LightGray, fontSize = 18.sp)
        }

        // Annehmen / Ablehnen Buttons (Unten)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 64.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Ablehnen (Rot)
            FloatingActionButton(
                onClick = onDeclineCall,
                containerColor = Color(0xFFD32F2F),
                shape = CircleShape,
                modifier = Modifier.size(72.dp)
            ) {
                Icon(Icons.Filled.CallEnd, contentDescription = "Ablehnen", tint = Color.White, modifier = Modifier.size(36.dp))
            }

            // Annehmen (Grün)
            FloatingActionButton(
                onClick = onAcceptCall,
                containerColor = Color(0xFF4CAF50),
                shape = CircleShape,
                modifier = Modifier.size(72.dp)
            ) {
                Icon(Icons.Filled.Call, contentDescription = "Annehmen", tint = Color.White, modifier = Modifier.size(36.dp))
            }
        }
    }
}