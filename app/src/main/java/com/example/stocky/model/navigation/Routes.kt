package com.example.stocky.model.navigation

sealed class Routes(val route: String) {
    object HomeScreen: Routes("home_screen")
    object ProductsScreen: Routes("products_screen")
    object AddProductScreen: Routes("add_product_screen")
    object ProductDetailScreen: Routes("product_detail_screen")
}