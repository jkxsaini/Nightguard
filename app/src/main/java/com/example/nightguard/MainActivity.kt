package com.example.nightguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.nightguard.ui.theme.NightguardTheme
import com.example.nightguard.navigation.NightguardNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NightguardTheme {
                val navController = rememberNavController()
                NightguardNavHost(navController = navController)
            }
        }
    }
}