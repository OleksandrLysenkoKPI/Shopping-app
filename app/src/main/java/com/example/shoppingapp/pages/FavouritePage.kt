package com.example.shoppingapp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppingapp.AppUtil
import com.example.shoppingapp.components.ProductItemView
import com.example.shoppingapp.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun FavouritePage(modifier: Modifier = Modifier) {
    val productsList = remember { mutableStateOf<List<ProductModel>>(emptyList()) }
    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val favouriteList = AppUtil.getFavouriteList(context)

        if (favouriteList.isNotEmpty()) {
            Firebase.firestore.collection("data").document("stock")
                .collection("products")
                .whereIn("id", favouriteList.toList())
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val resultList = it.result.documents.mapNotNull { doc ->
                            doc.toObject(ProductModel::class.java)
                        }
                        productsList.value = resultList
                    }
                    isLoading.value = false
                }
        } else {
            productsList.value = emptyList()
            isLoading.value = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your Favourite List",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )

        if (!isLoading.value && productsList.value.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No favourites yet",
                    color = Color.Gray
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(productsList.value) { product ->
                    ProductItemView(
                        product = product,
                        isFav = true,

                        onRemove = {
                            productsList.value = productsList.value.filter { it.id != product.id }
                        }
                    )
                }
            }
        }
    }
}