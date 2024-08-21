package com.example.stocky.presentation.sales

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.stocky.model.product.Product
import com.example.stocky.model.sale.Sale
import com.example.stocky.presentation.sales.util.PaymentMethods
import com.example.stocky.presentation.viewmodels.SharedViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

private const val SELECCIONAR_METODO_DE_PAGO = "Seleccionar metodo de pago"

@Composable
fun AddSaleScreen(
    viewModel: SharedViewModel,
    onGoToProductSelection: () -> Unit
) {
    val confirmedProducts = viewModel.confirmedProducts.observeAsState()

    val paymentMethod = remember { mutableStateOf(SELECCIONAR_METODO_DE_PAGO) }
    val expanded = remember { mutableStateOf(false) }

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .padding(12.dp)
    ) {

        val (headerSpacer, btnProductSelection, btnPaymentMethod, sellDetails, sellTotalAmount, btnConfirm) = createRefs()

        Spacer(modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .constrainAs(headerSpacer) {
                top.linkTo(parent.top)
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(56.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onGoToProductSelection() }
                .constrainAs(btnProductSelection) {
                    top.linkTo(headerSpacer.bottom)
                }
        ) {
            Surface(
                shadowElevation = 4.dp,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 14.dp),
                        text = "Ir a seleccion de productos",
                        color = Color.Black
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(56.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { expanded.value = true }
                .constrainAs(btnPaymentMethod) {
                    top.linkTo(btnProductSelection.bottom)
                }
        ) {
            Surface(
                shadowElevation = 4.dp,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        modifier = Modifier.padding(start = 14.dp),
                        text = paymentMethod.value,
                        color = if (paymentMethod.value == SELECCIONAR_METODO_DE_PAGO) Color.Gray else Color.Black
                    )
                }

                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    PaymentMethods.values().forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = it.id(),
                                    color = Color.Black
                                )
                            },
                            onClick = {
                                paymentMethod.value = it.id()
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
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
            .constrainAs(sellDetails) {
                top.linkTo(btnPaymentMethod.bottom)
                bottom.linkTo(sellTotalAmount.top)
                height = Dimension.fillToConstraints
            }
        ) {
            Text(
                text = "Detalle de venta: ",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 48.dp,
                        bottom = 12.dp
                    )
            ) {
                item {
                    confirmedProducts.value?.forEach { product ->
                        val noCost = viewModel.selectedNoCost.value?.contains(product.id) ?: false
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = product.name,
                                    modifier = Modifier.align(Alignment.CenterStart)
                                )
                                Text(
                                    text = if (noCost) "$0" else product.price,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        }
                    }
                }
            }
        }
        
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .constrainAs(sellTotalAmount) {
                bottom.linkTo(btnConfirm.top)
            }
        ) {
            val products = confirmedProducts.value.orEmpty()
            val isNotEmptyProducts = products.isNotEmpty()
            val totalMessage = if (isNotEmptyProducts) getTotalPrice(products, viewModel.selectedNoCost.value) else "$0"
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .height(1.dp)
                .background(Color.Black)
            )
            Text(
                text = "Total: $totalMessage",
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.BottomEnd)
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .constrainAs(btnConfirm) {
                bottom.linkTo(parent.bottom)
            }
        ) {
            Button(
                onClick = {
                    confirmedProducts.value?.let { products ->
                        if (isReadyToAddSale(products, paymentMethod.value)) {
                            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                            val now = Date()
                            val finalProducts = products.map { product ->
                                if (viewModel.selectedNoCost.value?.contains(product.id) == true) {
                                    product.copy(price = "$0")
                                } else {
                                    product
                                }
                            }
                            viewModel.addSale(
                                Sale(
                                    id = UUID.randomUUID().toString(),
                                    products = finalProducts,
                                    totalPrice = getTotalPrice(products, viewModel.selectedNoCost.value),
                                    paymentMethod = paymentMethod.value,
                                    date = dateFormat.format(now),
                                    hour = timeFormat.format(now)
                                )
                            )
                            viewModel.resetSelectedProducts()
                            viewModel.resetConfirmedProducts()
                            paymentMethod.value = SELECCIONAR_METODO_DE_PAGO
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(
                    text = "Confirmar Venta",
                    fontSize = 24.sp
                )
            }
        }
    }
}

private fun isReadyToAddSale(confirmedProducts: List<Product>, paymentMethods: String): Boolean {
    return confirmedProducts.isNotEmpty() && paymentMethods != SELECCIONAR_METODO_DE_PAGO
}

private fun getTotalPrice(confirmedProducts: List<Product>, noCostProducts: List<String>?): String {
    val finalList = noCostProducts?.let { noCostList ->
        confirmedProducts.filter { !noCostList.contains(it.id) }
    } ?: confirmedProducts
    val totalPrice = finalList.sumOf { parseCurrency(it.price) }
    return formatCurrency(totalPrice)
}

private fun parseCurrency(formattedValue: String): Int {
    val numberString = formattedValue.replace("$", "").replace(".", "").replace(",", "")
    return numberString.toInt()
}

private fun formatCurrency(value: Int): String {
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = '.'
    }
    val format = DecimalFormat("#,##0", symbols)
    return "$${format.format(value)}"
}