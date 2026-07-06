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
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

import com.example.nightguard.AlarmScreen
import com.example.nightguard.FakeCallScreen
import com.example.nightguard.MainScreen
import com.example.nightguard.SOSscreen
import com.example.nightguard.UserProfileScreen
import com.example.nightguard.ui.AlarmViewModel
import com.example.nightguard.LocationViewModel
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

fun navigateToUserProfileScreen(navController: NavController) {
    navController.navigate("UserProfile")
}
@Composable
fun NightguardNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val locationViewModel: LocationViewModel = viewModel()
    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

        val coarseLocationGranted =
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fineLocationGranted || coarseLocationGranted) {
            locationViewModel.onLocationPermissionDenied()
        }
    }

    NavHost(
        navController = navController,
        startDestination = "Main",
        modifier = modifier
    ) {
        composable("Main") {
            MainScreen(
                modifier = Modifier,
                locationUiState = locationViewModel.locationUiState,
                onShareLocationClick = {
                    val fineLocationGranted = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    val coarseLocationGranted = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    if (fineLocationGranted || coarseLocationGranted) {
                        locationViewModel.loadCurrentLocation()
                    } else {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }

                },
                onFakeCallClick = {
                    navigateToFakeCall(navController)
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

        composable("UserProfile") {
            UserProfileScreen(
                onBackToHome = {
                    navController.navigate("Main")
                }
            )
        }
    }
}