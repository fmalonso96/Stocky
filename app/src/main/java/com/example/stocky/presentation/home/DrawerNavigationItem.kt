package com.example.stocky.presentation.home

import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val destinationId: String? = null,
    val onItemClicked: () -> Unit = {}
)