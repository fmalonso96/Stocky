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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stocky.ui.theme.StockyTheme
import com.example.stocky.presentation.login.LoginScreen
import com.example.stocky.presentation.login.GoogleAuthClient
import com.example.stocky.presentation.login.LoginViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthClient by lazy {
        GoogleAuthClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            StockyTheme {
                val context = LocalContext.current
                val activity = LocalContext.current as Activity

                val viewModel = viewModel<LoginViewModel>()
                val signInState by viewModel.signInState.collectAsState()
                val isUserLogged by viewModel.isUserLogged.collectAsState()
                val foundUser = firebaseAuth.currentUser != null

                //Common sign in.
                val isLoginSuccessful by viewModel.isLoginSuccessful.collectAsState()
                val loginError by viewModel.loginError.collectAsState()

                splashScreen.setKeepOnScreenCondition { foundUser }

                if (foundUser) {
                    context.startActivity(Intent(context, HomeActivity::class.java))
                    activity.finish()
                }
                if (isUserLogged) {
                    viewModel.setLoading(false)
                }

                if (isLoginSuccessful) {
                    context.startActivity(Intent(context, HomeActivity::class.java))
                    viewModel.resetLoginState()
                }
                if (loginError != null) {
                    viewModel.resetLoginError()
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
                        } else {
                            viewModel.setLoading(false)
                        }
                    }
                )

                AnimatedVisibility(visible = signInState.isSignInSuccessful) {
                    LaunchedEffect(key1 = Unit) {
                        context.startActivity(Intent(context, HomeActivity::class.java))
                        viewModel.resetSignInState()
                        activity.finish()
                    }
                }

                LoginScreen(
                    viewModel,
                    signInState,
                    onCommonSignIn = {
                        viewModel.setLoading(true)
                        viewModel.loginWithEmailAndPassword(firebaseAuth)
                    },
                    onSignInClick = {
                        viewModel.setLoading(true)
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
        }
    }
}
