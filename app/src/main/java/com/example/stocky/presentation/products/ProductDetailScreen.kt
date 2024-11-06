package com.example.stocky.presentation.products

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.stocky.model.product.Product
import com.example.stocky.presentation.products.util.ProductCategories
import com.example.stocky.presentation.viewmodels.SharedViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

private const val SELECCIONAR_CATEGORIA = "Seleccionar Categoría"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(viewModel: SharedViewModel, onUpdateProduct: () -> Unit) {

    val product = viewModel.currentProduct
    val category = remember { mutableStateOf(product.category) }
    val name = remember { mutableStateOf(product.name) }
    val price = remember { mutableStateOf(parseCurrency(product.price)) }

    val expanded = remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
    ) {
        Spacer(modifier = Modifier
            .height(60.dp)
            .fillMaxWidth())

        Box(modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier.fillMaxWidth()){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(4.dp))
                        .height(56.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { expanded.value = true }
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterStart)
                            .padding(start = 14.dp),
                        text = category.value.ifEmpty { SELECCIONAR_CATEGORIA },
                        color = Color.Black
                    )

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        ProductCategories.values().forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = it.id(),
                                        color = Color.Black
                                    )
                                },
                                onClick = {
                                    category.value = it.id()
                                    expanded.value = false
                                },
                                modifier = Modifier
                                    .indication(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    )
                            )
                        }
                    }
                }

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = name.value,
                    onValueChange = { name.value = it },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedBorderColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray
                    ),
                    label = { Text(text = "Nombre") },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = price.value,
                    onValueChange = { price.value = it },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedBorderColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray
                    ),
                    label = { Text(text = "Precio") },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .align(Alignment.BottomCenter)
                    .height(48.dp),
                onClick = {
                    if (isReadyToAdd(name.value, price.value, category.value)) {
                        viewModel.updateProduct(
                            Product(
                                id = product.id,
                                name = name.value,
                                price = formatCurrency(price.value.toInt()),
                                category = category.value
                            )
                        )
                        focusManager.clearFocus()
                        onUpdateProduct()
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Actualizar Producto")
            }
        }
    }
}

private fun isReadyToAdd(name: String, price: String, category: String): Boolean {
    val isPriceCorrect = price.isNotEmpty() && price.all { it.isDigit() }
    return (name.isNotEmpty() && isPriceCorrect && category.isNotEmpty()) && category != SELECCIONAR_CATEGORIA
}

private fun formatCurrency(value: Int): String {
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = '.'
    }
    val format = DecimalFormat("#,##0", symbols)
    return "$${format.format(value)}"
}

private fun parseCurrency(formattedValue: String): String {
    val numberString = formattedValue.replace("$", "").replace(".", "").replace(",", "")
    return numberString
}
