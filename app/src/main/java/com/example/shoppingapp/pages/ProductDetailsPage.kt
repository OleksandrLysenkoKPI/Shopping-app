package com.example.shoppingapp.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.shoppingapp.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun ProductsDetailsPage(modifier: Modifier = Modifier, productId: String) {
    var product by remember {
        mutableStateOf(ProductModel())
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data"). document("stock")
            .collection("products")
            .document(productId).get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    var result = it.result.toObject(ProductModel::class.java)

                    if(result != null) {
                        product = result
                    }

                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = product.title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(Modifier.height(8.dp))

        ProductImageViewer(product)

        Spacer(Modifier.height(24.dp))

        ProductPriceViewer(product)

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Product description :",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = product.description,
            fontSize = 16.sp
        )

        OtherDetailsViewer(product)
    }
}

@Composable
fun ProductImageViewer(product: ProductModel) {
    Column {
        val pagerState = rememberPagerState(0) {
            product.images.size
        }
        HorizontalPager(
            state = pagerState,
            pageSpacing = 24.dp
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AsyncImage(
                    model =  product.images[page],
                    contentDescription = "Product images",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        DotsIndicator(
            dotCount =  product.images.size,
            type = ShiftIndicatorType(DotGraphic(
                color = MaterialTheme.colorScheme.primary,
                size = 6.dp
            )),
            pagerState = pagerState
        )

    }
}

@Composable
fun ProductPriceViewer(product: ProductModel) {
    // Old price (if present)
    if (product.actualPrice != product.price) {
        Text(
            text = "$${product.price}",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(textDecoration = TextDecoration.LineThrough)
        )
        Spacer(modifier = Modifier.height(2.dp))
    }

    // New price + cart button
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$${product.actualPrice}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {
            // TODO
        }) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Add to Favourite",
                modifier = Modifier.size(32.dp)
            )
        }
    }

    Button(onClick = {
        // TODO
    },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Add to Cart",
            fontSize = 16.sp
        )
    }
}

@Composable
fun OtherDetailsViewer(product: ProductModel) {
    if (product.otherDetails.isNotEmpty()) {
        Spacer(Modifier.height(8.dp))

        Text(
            text = "Other product details :",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))

        product.otherDetails.forEach { (key, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(text = "$key: ", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(text = value, fontSize = 16.sp)
            }
        }
    }
}