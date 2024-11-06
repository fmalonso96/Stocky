package com.example.stocky.presentation.login

data class SignInResult(
    val data: UserInformation?,
    val errorMessage: String?
)

data class UserInformation(
    val userId: String,
    val username: String?
)