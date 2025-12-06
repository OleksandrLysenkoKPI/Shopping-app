package com.example.shoppingapp.model

// Doesn't store password because password handled by authentication
data class UserModel(
    val name: String,
    val email: String,
    val uid: String
)
