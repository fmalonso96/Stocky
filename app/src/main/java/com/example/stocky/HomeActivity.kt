package com.example.stocky

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stocky.model.navigation.Routes.AddProductScreen
import com.example.stocky.model.navigation.Routes.HomeScreen
import com.example.stocky.model.navigation.Routes.ProductDetailScreen
import com.example.stocky.presentation.home.HomeScreen
import com.example.stocky.presentation.products.ProductDetailScreen
import com.example.stocky.presentation.products.ProductsScreen
import com.example.stocky.presentation.products.AddProductScreen
import com.example.stocky.model.navigation.Routes.ProductsScreen
import com.example.stocky.presentation.home.DrawerNavigationItem
import com.example.stocky.presentation.login.GoogleAuthClient
import com.example.stocky.presentation.viewmodels.ProductsViewModel
import com.example.stocky.ui.theme.StockyTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
class HomeActivity : ComponentActivity() {

    private val googleAuthClient by lazy {
        GoogleAuthClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel: ProductsViewModel = viewModel()

            StockyTheme {
                val context = LocalContext.current
                val activity = LocalContext.current as Activity

                var toolbarTitle by remember { mutableStateOf(DrawerNavigationItem.DrawerItemHome.title) }

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val coroutineScope = rememberCoroutineScope()
                var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

                val navController = rememberNavController()

                ModalNavigationDrawer(
                    modifier = Modifier.fillMaxSize(),
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            Spacer(modifier = Modifier.height(16.dp))
                            setupDrawerActivityItems().forEachIndexed { index, item ->
                                NavigationDrawerItem(
                                    label = { Text(text = item.title) },
                                    selected = index == selectedItemIndex,
                                    onClick = {
                                        when (item.title) {
                                            DrawerNavigationItem.DrawerItemHome.title -> {
                                                navController.navigate(HomeScreen.route)
                                                toolbarTitle = item.title
                                            }
                                            DrawerNavigationItem.DrawerItemProducts.title -> {
                                                navController.navigate(ProductsScreen.route)
                                                toolbarTitle = item.title
                                            }
                                            DrawerNavigationItem.DrawerItemStorage.title -> {}
                                            DrawerNavigationItem.DrawerItemSettings.title -> {}
                                            DrawerNavigationItem.DrawerItemSignOut.title -> {
                                                setSignOutNavigation(lifecycleScope, googleAuthClient, context, activity)
                                            }
                                        }
                                        selectedItemIndex = index
                                        coroutineScope.launch { drawerState.close() }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (selectedItemIndex == index) {
                                                item.selectedIcon
                                            } else {
                                                item.unselectedIcon
                                            },
                                            contentDescription = "Icon ${item.title}"
                                        )
                                    },
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                )
                            }
                        }
                    }
                ) {
                    Scaffold(
                        Modifier.fillMaxSize(),
                        topBar = { Toolbar(toolbarTitle, onMenuClicked = { coroutineScope.launch { drawerState.apply { open() } } }) },
                    ) {
                        NavHost(navController = navController, startDestination = HomeScreen.route) {
                            composable(HomeScreen.route) {
                                HomeScreen()
                            }
                            composable(ProductsScreen.route) {
                                ProductsScreen(
                                    viewModel,
                                    onAddProductClicked = {
                                        toolbarTitle = "Add Product"
                                        navController.navigate(AddProductScreen.route)
                                    },
                                    onProductClicked = {
                                        toolbarTitle = "Product Detail"
                                        navController.navigate(ProductDetailScreen.route)
                                    }
                                )
                            }
                            composable(AddProductScreen.route) {
                                AddProductScreen(viewModel)
                            }
                            composable(ProductDetailScreen.route) {
                                ProductDetailScreen(viewModel)
                            }
                        }
                    }
                }

                navController.addOnDestinationChangedListener { _, destination, _ ->
                    when (destination.route) {
                        HomeScreen.route -> toolbarTitle = "Home"
                        ProductsScreen.route -> toolbarTitle = "Products"
                        AddProductScreen.route -> toolbarTitle = "Add Product"
                        ProductDetailScreen.route -> toolbarTitle = "Product Detail"
                        //ADD THE REST OF ROUTES ONCE CREATED TO CHANGE TOOLBAR TITLE ON BACK PRESSED
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun Toolbar(title: String, onMenuClicked: () -> Unit) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onMenuClicked) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
        }
    )
}

fun setupDrawerActivityItems(): List<DrawerNavigationItem> = listOf(
    DrawerNavigationItem.DrawerItemHome,
    DrawerNavigationItem.DrawerItemProducts,
    DrawerNavigationItem.DrawerItemStorage,
    DrawerNavigationItem.DrawerItemSettings,
    DrawerNavigationItem.DrawerItemSignOut
)

private fun setSignOutNavigation(scope: CoroutineScope, auth: GoogleAuthClient, context: Context, activity: Activity) {
    scope.launch {
        auth.signOut()
    }
    Toast.makeText(
        context,
        "Signed Out",
        Toast.LENGTH_SHORT
    ).show()
    context.startActivity(Intent(context, MainActivity::class.java))
    activity.finish()
}