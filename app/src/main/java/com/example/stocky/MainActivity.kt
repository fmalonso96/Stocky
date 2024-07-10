package com.example.stocky

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stocky.ui.theme.StockyTheme
import com.example.stocky.presentation.login.LoginScreen
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
                val context = LocalContext.current
                val activity = LocalContext.current as Activity

                val viewModel = viewModel<LoginViewModel>()
                val signInState by viewModel.signInState.collectAsState()

                LaunchedEffect(key1 = Unit) {
                    if (googleAuthClient.getSignedInUser() != null) {
                        context.startActivity(Intent(context, HomeActivity::class.java))
                        activity.finish()
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
                            context,
                            "Signed In",
                            Toast.LENGTH_SHORT
                        ).show()
                        context.startActivity(Intent(context, HomeActivity::class.java))
                        viewModel.resetSignInState()
                        activity.finish()
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
                    },
                    onLongPress = {
                        context.startActivity(Intent(context, HomeActivity::class.java))
                        activity.finish()
                    }
                )
            }
        }
    }
}
