package com.example.finaluri_shejibris_app


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finaluri_shejibris_app.Data.DatabaseProvider
import com.example.finaluri_shejibris_app.Data.ParticipantRepository
import com.example.finaluri_shejibris_app.screen.FinalScreen
import com.example.finaluri_shejibris_app.screen.LoginScreen
import com.example.finaluri_shejibris_app.screen.Round16

import com.example.finaluri_shejibris_app.vm.ParticipantsViewModel
import com.example.finaluri_shejibris_app.vm.ParticipantsViewModelFactory

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    val repository = ParticipantRepository(DatabaseProvider.db.participantDao())

    val participantsViewModel: ParticipantsViewModel = viewModel(
        factory = ParticipantsViewModelFactory(repository)
    )

    NavHost(
        navController = navController,
        startDestination = LoginRoute
    ) {

        composable<LoginRoute> {
            LoginScreen(
                viewModel = participantsViewModel,
                onLoginSuccess = {
                    navController.navigate(Round16Route)
                }
            )
        }

        composable<Round16Route> {

            Round16(
                viewModel = participantsViewModel,
                onFinish = {
                    navController.navigate(FinalRoute)
                }
            )
        }
        composable<FinalRoute> {
            FinalScreen(
                viewModel = participantsViewModel,
                onBackToLogin = {
                    navController.navigate(LoginRoute) {
                        popUpTo(0)
                    }
                }
            )
        }


    }
}