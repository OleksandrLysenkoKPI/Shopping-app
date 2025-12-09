package com.example.shoppingapp.model

// Number of fields here = number of fields in Firestore Database
data class ProductModel(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val actualPrice: String = "",
    val category: String = "",
    val images: List<String> = emptyList(),
    val otherDetails: Map<String, String> = mapOf()
)
