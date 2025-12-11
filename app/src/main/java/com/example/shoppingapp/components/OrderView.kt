package com.example.shoppingapp.components

import android.R.attr.order
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppingapp.AppUtil
import com.example.shoppingapp.GlobalNavigation
import com.example.shoppingapp.model.OrderModel

@Composable
fun OrderView(orderItem: OrderModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(8.dp)
            .fillMaxWidth()
            .clickable{
                GlobalNavigation.navController.navigate("order-details/${orderItem.id}")
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Order ID: ${orderItem.id}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = AppUtil.formatDate(orderItem.date),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            val statusColor = when (orderItem.status) {
                "DELIVERED" -> Color(0xFF4CAF50)
                "ON THE WAY" -> Color(0xFFFCBF08)
                "CANCELED" -> Color.Red
                else -> Color(0xFFFF5722)
            }

            Text(
                text = orderItem.status,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = statusColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${orderItem.items.size} items",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}