package com.example.stocky.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.stocky.login.Bottom

@Composable
fun HomeScreen(navController: NavHostController) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Bottom(Modifier.align(Alignment.BottomCenter), navController)
    }
}