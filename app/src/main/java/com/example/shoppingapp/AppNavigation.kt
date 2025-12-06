package com.example.shoppingapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.screens.AuthScreen
import com.example.shoppingapp.screens.LoginScreen
import com.example.shoppingapp.screens.SignupScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth") {

        composable("auth"){
            AuthScreen(modifier, navController)
        }

        composable("login"){
            LoginScreen(modifier)
        }

        composable("signup"){
            SignupScreen(modifier)
        }
    }
}