package com.example.shoppingapp

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.ui.text.toUpperCase
import com.example.shoppingapp.model.OrderModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

object AppUtil {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun addToCart(productId: String, context: Context){

        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful){
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId] ?: 0
                val updatedQuantity = currentQuantity + 1

                val updatedCart = mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updatedCart)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            showToast(context, "Item added to the cart")
                        } else {
                            showToast(context, "Failed to add item to the cart")
                        }
                    }
            }
        }
    }

    fun removeFromCart(productId: String, context: Context, removeAll: Boolean = false){

        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful){
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId] ?: 0
                val updatedQuantity = currentQuantity - 1

                val updatedCart =
                    if (updatedQuantity <= 0 || removeAll){
                        mapOf("cartItems.$productId" to FieldValue.delete())
                    } else {
                        mapOf("cartItems.$productId" to updatedQuantity)
                    }

                userDoc.update(updatedCart)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            showToast(context, "Item removed from cart")
                        } else {
                            showToast(context, "Failed to remove item from cart")
                        }
                    }
            }
        }
    }

    fun clearCartAndAddToOrders(
        context: Context,
        onSuccess: () -> Unit, // Callback для успіху
        onFailure: (String) -> Unit // Callback для помилки
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            onFailure("User not logged in")
            return
        }

        val userDoc = Firebase.firestore.collection("users").document(userId)

        Handler(Looper.getMainLooper()).postDelayed({

            userDoc.get().addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val currentCart = task.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                    val address = task.result.getString("address") ?: ""


                    if (currentCart.isEmpty()) {
                        onFailure("Cart is empty")
                        return@addOnCompleteListener
                    }


                    val order = OrderModel(
                        id = "ORD_" + UUID.randomUUID().toString().replace("-", "").take(10).uppercase(),
                        userId = userId,
                        date = Timestamp.now(),
                        items = currentCart,
                        status = "ORDERED",
                        address = address
                    )

                    val firestore = Firebase.firestore
                    val batch = firestore.batch()

                    val orderRef = firestore.collection("orders").document(order.id)
                    batch.set(orderRef, order)

                    batch.update(userDoc, "cartItems", emptyMap<String, Long>())

                    batch.commit()
                        .addOnSuccessListener {
                            showToast(context, "Payment Successful!")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            showToast(context, "Order failed: ${e.message}")
                            onFailure(e.message ?: "Unknown error")
                        }
                } else {
                    onFailure("Failed to get user data")
                }
            }
        }, 2000)
    }

    fun getDiscountPercentage(): Float = 10.0f
    fun getTaxPercentage(): Float = 20.0f

    fun formatDate(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        return sdf.format(timestamp.toDate().time)
    }
}