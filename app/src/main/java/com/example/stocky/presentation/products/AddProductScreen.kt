package com.example.stocky.presentation.products

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stocky.model.product.Product
import com.example.stocky.presentation.viewmodels.ProductsViewModel
import java.util.*

@Composable
fun AddProductScreen(viewModel: ProductsViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
    ) {
        Spacer(modifier = Modifier
            .height(64.dp)
            .fillMaxWidth())
        Text(text = "Add Product Screen")
        Box(modifier = Modifier.fillMaxSize()) {
            Button(
                onClick = {
                    viewModel.addProduct(
                        Product(
                            UUID.randomUUID().toString(),
                            "Ravioles",
                            "7000",
                            "Pasta"
                        )
                    )
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(text = "Agregar")
            }
        }
    }
}