package com.example.shoppingapp.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shoppingapp.components.BannerView
import com.example.shoppingapp.components.HeaderView

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(20.dp)
    ) {
        HeaderView(modifier)
        BannerView(modifier)
    }
}