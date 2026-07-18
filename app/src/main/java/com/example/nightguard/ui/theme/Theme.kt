package com.example.nightguard.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp



private val DarkColorScheme = darkColorScheme(
    background = NightguardBackground,
    primary = GlassWhite,
    onBackground = Color.White,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    surface = Black80
)
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color.White
)

@Composable
fun NightguardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Auf false gesetzt, damit DEINE Farben Priorität haben
    content: @Composable () -> Unit
){
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


// Metallic Button effekt
fun Modifier.metallicGlassBorder() = this.border(
    width = 1.dp,
    brush = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.5f), // Glanzpunkt
            Color.Transparent,             // Durchsichtige Mitte
            Color.White.copy(alpha = 0.2f)  // Subtiler Abschluss
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    ),
    shape = RoundedCornerShape(12.dp)
)

@Composable
fun metallicGlassButtonColors() = ButtonDefaults.buttonColors(
    containerColor = Color.White.copy(alpha = 0.18f), // Hauch von Glas
    contentColor = Color.White,
    disabledContainerColor = Color.White.copy(alpha = 0.08f),
    disabledContentColor = Color.White.copy(alpha = 0.45f)
)

@Composable
fun glassCardColors() = CardDefaults.cardColors(
    containerColor = Color.White.copy(alpha = 0.03f), // Sehr dezent, damit es edel wirkt
    contentColor = Color.White
)


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */