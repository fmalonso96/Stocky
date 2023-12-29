package com.example.stocky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.stocky.ui.theme.StockyTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stocky.login.LoginScreen
import com.example.stocky.model.Routes.LoginScreen
import com.example.stocky.model.Routes.HomeScreen
import com.example.stocky.home.HomeScreen
import com.example.stocky.login.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockyTheme {
                Box(Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = LoginScreen.route) {
                        composable(LoginScreen.route) { LoginScreen(navController, LoginViewModel()) }
                        composable(HomeScreen.route) { HomeScreen(navController) }
                        //composable(Routes.StockScreen.route) {}
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StockyTheme {

    }
}
