package com.example.stocky.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun HomeScreen(navController: NavHostController, onSignOut: () -> Unit) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerItems() }
    ) {
        Scaffold(
            Modifier.fillMaxSize(),
            topBar = {
                Toolbar(
                    onMenuClicked = {
                        coroutineScope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            }
        ) {

            Button(onClick = onSignOut) {
                Text(text = "Sign Out")
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun Toolbar(onMenuClicked: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Home", fontWeight = FontWeight.Bold) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Black,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        navigationIcon = {
            IconButton(onClick = onMenuClicked) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun DrawerItems() {
    ModalDrawerSheet {
        Text("Menu", modifier = Modifier.padding(16.dp))
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Opcion 1") },
            selected = false,
            onClick = { /*TODO*/ }
        )
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Opcion 2") },
            selected = false,
            onClick = { /*TODO*/ }
        )
        Divider()
        NavigationDrawerItem(
            label = { Text(text = "Opcion 3") },
            selected = false,
            onClick = { /*TODO*/ }
        )
    }
}