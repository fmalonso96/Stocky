package com.example.stocky.presentation.products

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.stocky.model.product.Product
import com.example.stocky.presentation.viewmodels.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    viewModel: SharedViewModel,
    onAddProductClicked: () -> Unit,
    onProductClicked: () -> Unit
) {

    val products = viewModel.products.observeAsState()
    val search = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp).fillMaxWidth())

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            value = search.value,
            onValueChange = { search.value = it },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                unfocusedLabelColor = Color.Gray
            ),
            label = { Text(text = "Buscador") },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            trailingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") }
        )

        Spacer(modifier = Modifier
            .height(24.dp)
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
fun ProductItem(product: Product, viewModel: SharedViewModel, onProductClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                viewModel.currentProduct = product
                onProductClicked()
            }
            .padding(vertical = 8.dp)
            .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(4.dp))
            .height(56.dp)
    ) {
        Text(
            text = product.name,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .align(Alignment.CenterStart)
        )
        Text(
            text = product.price,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .align(Alignment.CenterEnd)
        )
    }
}