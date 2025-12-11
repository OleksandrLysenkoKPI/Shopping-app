package com.example.shoppingapp.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppingapp.components.OrderView
import com.example.shoppingapp.model.OrderModel
import com.example.shoppingapp.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlin.jvm.java

@Composable
fun OrdersPage(modifier: Modifier = Modifier) {
    val ordersList = remember {mutableStateOf<List<OrderModel>>(emptyList())}

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("orders")
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener(){
                if (it.isSuccessful){
                    val resultList = it.result.documents.mapNotNull { doc ->
                        doc.toObject(OrderModel::class.java)
                    }
                    ordersList.value = resultList
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your orders",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            val sortedOrders = ordersList.value.sortedByDescending { it.date }

            items(sortedOrders) { order ->
                OrderView(orderItem = order)
            }
        }
    }
}