package com.example.stocky.presentation.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stocky.model.product.Product
import com.example.stocky.presentation.viewmodels.ProductsViewModel

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel,
    onAddProductClicked: () -> Unit,
    onProductClicked: () -> Unit
) {

    val products = viewModel.products.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
    ) {
        Spacer(modifier = Modifier
            .height(64.dp)
            .fillMaxWidth())
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                products.value?.forEach { product ->
                    item { ProductItem(product, viewModel, onProductClicked) }
                }
            }
            FloatingActionButton(
                onClick = { onAddProductClicked() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, viewModel: ProductsViewModel, onProductClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                viewModel.currentProduct = product
                onProductClicked()
            }
    ) {
        Text(text = product.name)
        Text(text = product.price)
    }
}