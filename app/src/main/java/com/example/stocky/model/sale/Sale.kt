package com.example.stocky.model.sale

import com.example.stocky.model.product.Product

data class Sale(
    val id: String = "",
    val products: List<Product> = listOf(),
    val totalPrice: String = "",
    val paymentMethod: String = "",
    val date: String = "",
    val hour: String = ""
)