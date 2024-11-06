package com.example.stocky.model.navigation

sealed class Routes(val route: String) {
    object HomeScreen: Routes("home_screen")
    object ProductsScreen: Routes("products_screen")
    object AddProductScreen: Routes("add_product_screen")
    object ProductDetailScreen: Routes("product_detail_screen")
    object SalesScreen: Routes("sales_screen")
    object AddSaleScreen: Routes("add_sales_screen")
    object ProductSelectionScreen: Routes("product_selection_screen")
    object MetricsScreen: Routes("metrics_screen")
}