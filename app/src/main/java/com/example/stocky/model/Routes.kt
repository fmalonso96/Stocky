package com.example.stocky.model

sealed class Routes(val route: String) {
    object LoginScreen: Routes("login_screen")
    object HomeScreen: Routes("home_screen")
    //object StockScreen: Routes("stock")
}