package com.example.stocky.presentation.metrics

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.stocky.presentation.viewmodels.SharedViewModel
import java.util.*

@Composable
fun MetricsScreen(viewModel: SharedViewModel) {

    val isLoading = viewModel.isLoading.observeAsState(initial = false)
    val productsMap = viewModel.productMetrics.observeAsState()

    val calendar = Calendar.getInstance(TimeZone.getDefault())
    val selectedStartDate = remember { mutableStateOf<Long?>(null) }
    val selectedEndDate = remember { mutableStateOf<Long?>(null) }
    val startDateText = remember { mutableStateOf("Desde: ") }
    val endDateText = remember { mutableStateOf("Hasta: ") }
    val showStartPicker = remember { mutableStateOf(false) }
    val showEndPicker = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadSales()
    }

    if (selectedStartDate.value != null && selectedEndDate.value != null) {
        viewModel.setSaleMetricsMap(selectedStartDate.value, selectedEndDate.value)
    }

    if (showStartPicker.value) {
        selectedStartDate.value?.let {
            calendar.apply {
                timeInMillis = it
            }
        }
        showStartDatePicker(selectedStartDate, startDateText, calendar, showStartPicker)
    }

    if (showEndPicker.value) {
        selectedEndDate.value?.let {
            calendar.apply {
                timeInMillis = it
            }
        }
        showEndDatePicker(selectedEndDate, endDateText, calendar, showEndPicker)
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
            text = "Ventas por fecha",
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
            if (selectedStartDate.value == null && selectedEndDate.value == null) {
                viewModel.setSaleMetricsMap(selectedStartDate.value, selectedEndDate.value)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shadowElevation = 4.dp,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .height(40.dp)
                        .clickable { showStartPicker.value = true },
                ) {
                    Row {
                        Text(
                            modifier = Modifier
                                .padding(start = 14.dp)
                                .align(Alignment.CenterVertically),
                            text = startDateText.value
                        )
                    }
                }
                Surface(
                    shadowElevation = 4.dp,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .height(40.dp)
                        .clickable { showEndPicker.value = true },
                ) {
                    Row {
                        Text(
                            modifier = Modifier
                                .padding(start = 14.dp)
                                .align(Alignment.CenterVertically),
                            text = endDateText.value
                        )
                    }
                }
            }
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

@Composable
fun showStartDatePicker(selectedDate: MutableState<Long?>, startDate: MutableState<String>, calendar: Calendar, show: MutableState<Boolean>) {
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            selectedDate.value = calendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }.timeInMillis
            startDate.value = "$dayOfMonth/${month + 1}/$year"
            show.value = false
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.setOnCancelListener {
        show.value = false
    }
    datePickerDialog.show()
}

@Composable
fun showEndDatePicker(selectedDate: MutableState<Long?>,endDate: MutableState<String>, calendar: Calendar, show: MutableState<Boolean>) {
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            selectedDate.value = calendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }.timeInMillis
            endDate.value = "$dayOfMonth/${month + 1}/$year"
            show.value = false
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.setOnCancelListener {
        show.value = false
    }
    datePickerDialog.show()
}