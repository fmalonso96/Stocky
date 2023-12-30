package com.example.stocky.presentation.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {

    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isLoginEnabled = MutableLiveData<Boolean>()
    val isLoginEnabled: LiveData<Boolean> = _isLoginEnabled

    private val _passwordVisibility = MutableLiveData<Boolean>()
    val passwordVisibility: LiveData<Boolean> = _passwordVisibility

    fun onSignInResult(result: SignInResult) {
        _signInState.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetSignInState() {
        _signInState.update { SignInState() }
    }

    fun onLoginChanged(newEmail: String, newPassword: String) {
        _email.value = newEmail
        _password.value = newPassword
        _isLoginEnabled.value = enableLogin(newEmail, newPassword)
    }

    private fun enableLogin(email: String, password: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 6
    }

    fun onPasswordVisibilityChanged() {
        val flag = _passwordVisibility.value ?: false
        _passwordVisibility.value = !flag
    }
}