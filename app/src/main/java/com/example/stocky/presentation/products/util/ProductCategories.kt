package com.example.stocky.presentation.products.util

import androidx.compose.ui.text.capitalize
import java.util.*

enum class ProductCategories {
    CARNE_POLLO_PESCADO,
    PASTAS,
    TARTAS_Y_TORTILLAS,
    MEDIAS_PORCIONES,
    FRIOS,
    GUARNICIONES,
    ESPECIALES;

    fun id(): String {
        return this.name.replace('_', '/')
    }
}