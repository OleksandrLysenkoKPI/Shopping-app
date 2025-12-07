package com.example.shoppingapp.pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun CategoryProductsPage(modifier: Modifier = Modifier, categoryId: String) {
    Text("Category product page :::::: " + categoryId)
}