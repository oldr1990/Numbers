package com.example.numbers.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.numbers.models.NumberItem
import com.example.numbers.screens.details.DetailsScreen
import com.example.numbers.screens.home.HomeScreen
import com.squareup.moshi.Moshi

@Composable
fun Navigator(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(navController = navController)
        }
        composable(Routes.DETAILS + "{number}") {
            val number = Moshi.Builder().build().adapter(NumberItem::class.java)
                .fromJson(it.arguments?.getString("number") ?: "")
            if (number == null) {
                navController.popBackStack()
            } else {
                DetailsScreen(navController = navController, number = number)
            }
        }
    }
}