package com.example.shoppingapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.pages.CategoryProductsPage
import com.example.shoppingapp.pages.ProductsDetailsPage
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

    val firebaseAuth = Firebase.auth
    val currentUser = firebaseAuth.currentUser

    val authCheckCompleted = remember { mutableStateOf(false) }
    val startDestination = remember { mutableStateOf("auth") }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            currentUser.getIdToken(false)
                .addOnCompleteListener { task ->
                    if ( task.isSuccessful) {
                        startDestination.value = "home"
                    } else {
                        firebaseAuth.signOut()
                        startDestination.value = "auth"
                    }
                    authCheckCompleted.value = true
                }
        } else {
            startDestination.value = "auth"
            authCheckCompleted.value = true
        }
    }

    if (authCheckCompleted.value) {
        NavHost(navController = navController, startDestination = startDestination.value) {

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

            composable("product-details/{productId}"){
                var productId = it.arguments?.getString("productId")
                ProductsDetailsPage(modifier, productId?:"")
            }
        }
    }
}

// Singleton
object GlobalNavigation {
    lateinit var navController : NavHostController
}