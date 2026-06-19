package com.example.nightguard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nightguard.ui.theme.NightguardTheme

@Composable
fun SosScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF350000))
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(85.dp))

        WarningBox()

        Spacer(modifier = Modifier.height(38.dp))

        Text(
            text = "SOS GESENDET!",
            color = Color(0xFFFF5A5A),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        CancelButton()

        Spacer(modifier = Modifier.height(20.dp))

        LiftAlarmButton()

        Spacer(modifier = Modifier.height(95.dp))
    }
}

@Composable
fun WarningBox() {
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(175.dp)
            .background(
                color = Color(0xFF5A5A5A),
                shape = RoundedCornerShape(18.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Ungewöhnliche\nBewegung\nerkannt!!",
            color = Color.White,
            fontSize = 23.sp,
            lineHeight = 31.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CancelButton() {
    Button(
        onClick = {
            // später: SOS abbrechen
        },
        modifier = Modifier
            .width(280.dp)
            .height(64.dp),
        shape = RoundedCornerShape(34.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6BC13B),
            contentColor = Color.White
        )
    ) {
        Text(
            text = "Abbrechen",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LiftAlarmButton() {
    Button(
        onClick = {
            // später: Alarm aufheben
        },
        modifier = Modifier
            .width(280.dp)
            .height(64.dp),
        shape = RoundedCornerShape(34.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF5B4B4B),
            contentColor = Color.White
        )
    ) {
        Text(
            text = "Alarm Aufheben",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SosScreenPreview() {
    NightguardTheme(
        darkTheme = true,
        dynamicColor = false
    ) {
        SosScreen()
    }
}