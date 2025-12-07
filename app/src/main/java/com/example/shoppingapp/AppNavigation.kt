package com.example.shoppingapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.pages.CategoryProductsPage
import com.example.shoppingapp.screens.AuthScreen
import com.example.shoppingapp.screens.HomeScreen
import com.example.shoppingapp.screens.LoginScreen
import com.example.shoppingapp.screens.SignupScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    GlobalNavigation.navController = navController

    val isLoggedIn = Firebase.auth.currentUser != null
    val firstPage = if(isLoggedIn) "home" else "auth"

    NavHost(navController = navController, startDestination = firstPage) {

        composable("auth"){
            AuthScreen(modifier, navController)
        }

        composable("login"){
            LoginScreen(modifier, navController)
        }

        composable("signup"){
            SignupScreen(modifier, navController)
        }

        composable("home"){
            HomeScreen(modifier, navController)
        }

        composable("category-products/{categoryId}"){
            var categoryId = it.arguments?.getString("categoryId")
            CategoryProductsPage(modifier, categoryId?:"")
        }
    }
}

// Singleton
object GlobalNavigation {
    lateinit var navController : NavHostController
}