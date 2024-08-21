package com.example.stocky.presentation.sales.util

import androidx.compose.ui.text.capitalize

enum class PaymentMethods {
    DEBITO,
    CREDITO,
    MP,
    EFECTIVO;

    fun id(): String {
        return this.name
    }
}

