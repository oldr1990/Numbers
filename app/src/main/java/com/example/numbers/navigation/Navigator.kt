package com.example.numbers.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.numbers.screens.home.HomeScreen

@Composable
fun Navigator(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.HOME){
        composable(Routes.HOME){
            HomeScreen(navController = navController)
        }
        composable(Routes.DETAILS + "{number}"){
          //  AddWordScreen(navController = navController)
        }
    }
}