package com.example.stocky.presentation.login

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)