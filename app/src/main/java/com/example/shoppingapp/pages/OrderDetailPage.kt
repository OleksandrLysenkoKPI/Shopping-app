package com.example.shoppingapp.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppingapp.AppUtil
import com.example.shoppingapp.GlobalNavigation
import com.example.shoppingapp.components.OrderDetailItemView
import com.example.shoppingapp.model.OrderModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailPage(orderId: String) {
    val orderModel = remember { mutableStateOf<OrderModel?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(orderId) {
        Firebase.firestore.collection("orders")
            .document(orderId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    orderModel.value = document.toObject(OrderModel::class.java)
                }
                isLoading.value = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details") },
                navigationIcon = {
                    IconButton(onClick = { GlobalNavigation.navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (orderModel.value != null) {
                val order = orderModel.value!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(text = "Order #${order.id}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "Date: ${AppUtil.formatDate(order.date)}", color = Color.Gray)

                    val statusColor = when (order.status) {
                        "DELIVERED" -> Color(0xFF4CAF50)
                        "ON THE WAY" -> Color(0xFFFBC02D)
                        "CANCELED" -> Color.Red
                        else -> Color(0xFFFF5722)
                    }
                    Text(text = "Status: ${order.status}", color = statusColor, fontWeight = FontWeight.SemiBold)

                    Text(text = "Address: ${order.address}", fontSize = 14.sp)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    Text(
                        text = "Items (${order.items.size})",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items(order.items.toList()) { (productId, qty) ->
                            OrderDetailItemView(productId = productId, qty = qty)
                        }
                    }
                }
            } else {
                Text("Order not found", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}