package com.example.nightguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.nightguard.ui.theme.NightguardTheme
import com.example.nightguard.navigation.NightguardNavHost // WICHTIG!
import org.osmdroid.config.Configuration
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().userAgentValue = applicationContext.packageName
        setContent {
            NightguardTheme {
                val navController = rememberNavController()
                NightguardNavHost(navController = navController)
            }
        }
    }
}