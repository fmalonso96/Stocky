package com.example.stocky.presentation.metrics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.stocky.presentation.viewmodels.SharedViewModel

@Composable
fun MetricsScreen(viewModel: SharedViewModel) {

    val isLoading = viewModel.isLoading.observeAsState(initial = false)
    val productsMap = viewModel.productMetrics.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSales()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
    ) {
        Spacer(modifier = Modifier
            .height(60.dp)
            .fillMaxWidth())
        Text(
            text = "Promedio de venta semanal",
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
        if (isLoading.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp), // Para mantener el padding horizontal
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            viewModel.setSaleMetricsMap()
            LazyColumn {
                productsMap.value?.forEach { (category, products) ->
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
                        }
                    }
                    items(products.size) {index ->
                        ProductMetricsItem(
                            products[index].first.name,
                            products[index].second
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductMetricsItem(name: String, quantity: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(4.dp))
            .height(56.dp)
    ) {
        Text(
            text = name,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .align(Alignment.CenterStart)
        )
        Text(
            text = quantity.toString(),
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .align(Alignment.CenterEnd)
        )
    }
}