package com.example.stocky.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Output
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Output
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingBasket
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DrawerNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object DrawerItemHome :DrawerNavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )
    object DrawerItemStorage :DrawerNavigationItem(
        title = "Storage",
        selectedIcon = Icons.Filled.Storage,
        unselectedIcon = Icons.Outlined.Storage
    )
    object DrawerItemSettings :DrawerNavigationItem(
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
    object DrawerItemSignOut :DrawerNavigationItem(
        title = "Sign Out",
        selectedIcon = Icons.Filled.Output,
        unselectedIcon = Icons.Outlined.Output
    )
    object DrawerItemProducts :DrawerNavigationItem(
        title = "Products",
        selectedIcon = Icons.Filled.ShoppingBasket,
        unselectedIcon = Icons.Outlined.ShoppingBasket
    )
}