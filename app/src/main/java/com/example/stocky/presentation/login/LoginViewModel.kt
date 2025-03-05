package com.example.stocky.presentation.login

import android.util.Log
import android.util.Patterns
import androidx.activity.result.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    //For google sign in.
    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isUserLogged = MutableStateFlow(false)
    val isUserLogged = _isUserLogged.asStateFlow()

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isLoginEnabled = MutableLiveData<Boolean>()
    val isLoginEnabled: LiveData<Boolean> = _isLoginEnabled

    // For common authentication method.
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()

    private val _isLoginSuccessful = MutableStateFlow(false)
    val isLoginSuccessful = _isLoginSuccessful.asStateFlow()

    private val _passwordVisibility = MutableLiveData<Boolean>()
    val passwordVisibility: LiveData<Boolean> = _passwordVisibility

    fun onSignInResult(result: SignInResult) {
        _signInState.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
        setUserLogged(result.data != null)
    }

    fun resetSignInState() {
        _signInState.update { SignInState() }
    }

    fun setLoading(loading: Boolean) {
        _isLoading.update { loading }
    }

    private fun setUserLogged(logged: Boolean) {
        _isUserLogged.update { logged }
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

    fun loginWithEmailAndPassword(auth: FirebaseAuth) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(_email.value.orEmpty(), _password.value.orEmpty())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            _isLoginSuccessful.update { true }
                            setUserLogged(true)
                        } else {
                            // If sign in fails, display a message to the user.
                            setLoginError(task.exception?.message)
                            setUserLogged(false)
                            setLoading(false)
                        }
                    }
            } catch (e: Exception) {
                setLoginError(e.message)
                setLoading(false)
            }
        }
    }

    private fun setLoginError(error: String?) {
        _loginError.update { error }
    }

    fun resetLoginError() {
        _loginError.update { null }
    }

    fun resetLoginState() {
        _isLoginSuccessful.update { false }
    }
}