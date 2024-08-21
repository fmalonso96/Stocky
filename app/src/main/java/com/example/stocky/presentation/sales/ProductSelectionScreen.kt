package com.example.stocky.presentation.sales

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stocky.model.product.Product
import com.example.stocky.presentation.viewmodels.SharedViewModel
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun ProductSelectionScreen(
    viewModel: SharedViewModel,
    onConfirmationClicked: () -> Unit
) {
    val products = viewModel.products.observeAsState()
    val selectedProducts = viewModel.selectedProductsQty.observeAsState()
    val selectedNoCost = viewModel.selectedNoCost.observeAsState()

    val groupedProducts = products.value?.groupBy { it.category }?.toSortedMap() ?: emptyMap()

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .padding(12.dp)
    ) {
        val (lazyColumn, button, header) = createRefs()

        Spacer(modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .constrainAs(header) {
                top.linkTo(parent.top)
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(lazyColumn) {
                    top.linkTo(header.bottom)
                    bottom.linkTo(button.top)
                    height = Dimension.fillToConstraints
                }
        ) {
            groupedProducts.forEach { (category, productsInCategory) ->
                item {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                    ) {
                        Text(
                            text = category,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic,
                                textDecoration = TextDecoration.Underline
                            ),
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        if (category == "GUARNICIONES") {
                            Text(
                                text = "$$",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic,
                                    textDecoration = TextDecoration.LineThrough
                                ),
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 109.dp)
                            )
                        }
                    }
                }

                items(productsInCategory.size) { index ->
                    val checkedNoCost = selectedNoCost.value?.contains(productsInCategory[index].id) ?: false
                    val amount = selectedProducts.value?.get(productsInCategory[index].id) ?: 0
                    ProductItem(
                        productsInCategory[index],
                        amount,
                        checkedNoCost,
                        onCheckedNoCostChange = { id ->
                            viewModel.setSelectedNoCost(id)
                        },
                        onQuantityChanged = { id, selectedAmount ->
                            viewModel.setSelectedProductsQty(id, selectedAmount)
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                viewModel.setConfirmedProducts(confirmSelection(products.value, selectedProducts.value))
                onConfirmationClicked()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(
                text = "Confirmar Productos",
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun ProductItem(
    data: Product,
    amount: Int,
    checkedNoCost: Boolean,
    onQuantityChanged: (String, Int) -> Unit,
    onCheckedNoCostChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = data.name,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (data.category == "GUARNICIONES") {
                Checkbox(
                    checked = checkedNoCost,
                    onCheckedChange = { onCheckedNoCostChange(data.id) }
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            QuantitySelector(amount) { quantity ->
                onQuantityChanged(data.id, quantity)
            }
        }
    }
}

@Composable
fun QuantitySelector(quantity: Int, onChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "-",
            fontSize = 18.sp,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { if (quantity > 0) onChange(quantity - 1) }
                .padding(4.dp)
        )
        Text(
            text = quantity.toString(),
            fontSize = 18.sp,
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "+",
            fontSize = 18.sp,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onChange(quantity + 1) }
                .padding(4.dp)
        )
    }
}

private fun confirmSelection(products: List<Product>?, selectedProducts: Map<String, Int>?): List<Product> {
    return selectedProducts?.let { selectedMap ->
        products?.let { prodList ->
            selectedMap.flatMap { (id, quantity) ->
                val product = prodList.find { it.id == id }
                if (product != null) {
                    List(quantity) { product }
                } else {
                    emptyList()
                }
            }
        } ?: emptyList()
    } ?: emptyList()
}