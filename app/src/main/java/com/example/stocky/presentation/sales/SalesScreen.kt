package com.example.stocky.presentation.sales

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.sp
import com.example.stocky.model.sale.Sale
import com.example.stocky.presentation.viewmodels.SharedViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

@Composable
fun SalesScreen(
    viewModel: SharedViewModel,
    onAddSaleClicked: () -> Unit
) {

    val sales = viewModel.sales.observeAsState()
    
    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text(
            text = sales.value?.let { totalProfit(it) } ?: "$0",
            modifier = Modifier.align(Alignment.Center),
            fontSize = 56.sp
        )
        Text(
            text = "Ventas: ${sales.value?.let { totalSells(it) } ?: "0"}",
            modifier = Modifier.align(Alignment.Center).padding(top = 96.dp)
        )
        FloatingActionButton(
            onClick = { onAddSaleClicked() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 8.dp, end = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Filled.Add, "Floating action button.")
        }
    }
}

private fun totalProfit(sales: List<Sale>): String {
    val salesList = sales.toMutableList()
    val total = salesList.sumOf { parseCurrency(it.totalPrice) }
    return formatCurrency(total)
}

private fun totalSells(sales: List<Sale>): String {
    return sales.size.toString()
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