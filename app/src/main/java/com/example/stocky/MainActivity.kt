package com.example.stocky

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stocky.ui.theme.StockyTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stocky.presentation.login.LoginScreen
import com.example.stocky.model.Routes.LoginScreen
import com.example.stocky.model.Routes.HomeScreen
import com.example.stocky.presentation.home.HomeScreen
import com.example.stocky.presentation.login.GoogleAuthClient
import com.example.stocky.presentation.login.LoginViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthClient by lazy {
        GoogleAuthClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockyTheme {
                Box(Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = LoginScreen.route) {
                        composable(LoginScreen.route) {
                            val viewModel = viewModel<LoginViewModel>()
                            val signInState by viewModel.signInState.collectAsState()

                            LaunchedEffect(key1 = Unit) {
                                if (googleAuthClient.getSignedInUser() != null) {
                                    navController.navigate(HomeScreen.route)
                                }
                            }

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(key1 = signInState.isSignInSuccessful) {
                                if (signInState.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Signed in",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate(HomeScreen.route)
                                    viewModel.resetSignInState()
                                }
                            }

                            LoginScreen(
                                viewModel,
                                signInState,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )
                        }

                        composable(HomeScreen.route) {
                            HomeScreen(
                                navController,
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed Out",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    navController.popBackStack()
                                }
                            )
                        }

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
