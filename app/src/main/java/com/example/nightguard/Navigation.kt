package com.example.nightguard.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nightguard.AlarmScreen
import com.example.nightguard.FakeCallScreen
import com.example.nightguard.MainScreen
import com.example.nightguard.SOSscreen
import com.example.nightguard.ui.AlarmViewModel
fun navigateToMain(navController: NavController) {
    navController.navigate("Main")
}

fun navigateToFakeCall(navController: NavController) {
    navController.navigate("FakeCall")
}

fun navigateToAlarm(navController: NavController) {
    navController.navigate("Alarm")
}

fun navigateToSOS(navController: NavController) {
    navController.navigate("SOS")
}
@Composable
fun NightguardNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "Main",
        modifier = modifier
    ) {
        composable("Main") {
            MainScreen(
                modifier = Modifier,
                onFakeCallClick = {navigateToFakeCall(navController)
        }
            )
        }
        composable("FakeCall") {
            FakeCallScreen(
                onAcceptCall = { navController.popBackStack() },
                onDeclineCall = { navController.popBackStack() }
            )
        }

        composable("Alarm") {
            val alarmViewModel: AlarmViewModel = viewModel()
            val timeLeft by alarmViewModel.timeLeft.collectAsState()

            LaunchedEffect(Unit) {
                alarmViewModel.startCountdown()
            }

            AlarmScreen(
                timeLeft = timeLeft,
                onCancelAlarm = {
                    alarmViewModel.cancelCountdown()
                    navigateToMain(navController)
                }
            )
        }

        composable("SOS") {
            SOSscreen(
                onCancelClick = {
                    navigateToMain(navController)
                }
            )
        }
    }
}