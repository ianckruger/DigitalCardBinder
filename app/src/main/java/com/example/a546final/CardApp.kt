package com.example.a546final

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun CardApp() {
    val navController = rememberNavController()
    val homeViewModel: HomeScreenViewModel = viewModel()
    val photoViewModel: PhotoViewModel = viewModel()

    NavHost(navController, startDestination = "home_screen") {
        composable("home_screen") {
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }

        composable("take_photo") {
            CameraScreen(
                navController = navController,
                homeScreenViewModel = homeViewModel,
            )
        }

        composable("view_cards") {
            BinderScreen(
                navController = navController,
                homeScreenViewModel = homeViewModel,
                photoViewModel = photoViewModel
            )
        }
    }
}
