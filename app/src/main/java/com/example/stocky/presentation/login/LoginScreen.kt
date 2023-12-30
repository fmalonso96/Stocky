package com.example.stocky.presentation.login

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stocky.ui.theme.Shapes

private const val CLOSE_ICON = "Close Icon"

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    signInState: SignInState,
    onSignInClick: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Header(Modifier.align(Alignment.TopEnd))
        Body(Modifier.align(Alignment.Center), loginViewModel, signInState, onSignInClick)
        Bottom(Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun Header(modifier: Modifier) {
    val activity = LocalContext.current as Activity
    Icon(
        imageVector = Icons.Default.Close,
        contentDescription = CLOSE_ICON,
        modifier = modifier.clickable { activity.finish() },
        tint = Color.Gray
    )
}

@Composable
fun Body(
    modifier: Modifier,
    loginViewModel: LoginViewModel,
    signInState: SignInState,
    onSignInClick: () -> Unit
) {

    val email: String by loginViewModel.email.observeAsState(initial = "")
    val password: String by loginViewModel.password.observeAsState(initial = "")
    val isLoginEnabled: Boolean by loginViewModel.isLoginEnabled.observeAsState(initial = false)

    Column(modifier = modifier) {
        Logo(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.size(16.dp))
        Email(email) {
            loginViewModel.onLoginChanged(newEmail = it, newPassword = password)
        }
        Spacer(modifier = Modifier.size(16.dp))
        Password(password, loginViewModel) {
            loginViewModel.onLoginChanged(newEmail = email, newPassword = it)
        }
        Spacer(modifier = Modifier.size(16.dp))
        ForgotPassword(Modifier.align(Alignment.End))
        Spacer(modifier = Modifier.size(16.dp))
        LoginButton(isLoginEnabled)
        Spacer(modifier = Modifier.size(16.dp))
        LoginDivider()
        Spacer(modifier = Modifier.size(16.dp))
        SocialLogin(signInState, onSignInClick)
    }
}

@Composable
fun Logo(modifier: Modifier) {
    Text(
        text = "Stocky",
        fontSize = 32.sp,
        color = Color.Gray,
        modifier = modifier.padding(bottom = 64.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Email(email: String, onEmailChanged: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = email,
        onValueChange = { onEmailChanged(it) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedBorderColor = Color.Gray,
            unfocusedLabelColor = Color.Gray
        ),
        label = { Text(text = "Email") },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Password(password: String, loginViewModel: LoginViewModel, onPasswordChanged: (String) -> Unit) {

    val passwordVisibility: Boolean by loginViewModel.passwordVisibility.observeAsState(initial = false)

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = password,
        onValueChange = { onPasswordChanged(it) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedBorderColor = Color.Gray,
            unfocusedLabelColor = Color.Gray
        ),
        label = { Text(text = "Password") },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisibility) {
                Icons.Filled.VisibilityOff
            } else {
                Icons.Filled.Visibility
            }
            IconButton(onClick = { loginViewModel.onPasswordVisibilityChanged() }) {
                Icon(imageVector = image, contentDescription = "Show Password")
            }
        },
        visualTransformation = if (passwordVisibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )
}

@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "Forgot Password?",
        color = Color.Blue,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
fun LoginButton(isLoginEnabled: Boolean) {
    Button(
        onClick = {  },
        modifier = Modifier.fillMaxWidth(),
        shape = Shapes.medium,
        enabled = isLoginEnabled,
        colors = ButtonDefaults.buttonColors(
             containerColor = Color.DarkGray,
             contentColor = Color.White,
             disabledContainerColor = Color.Gray,
             disabledContentColor = Color.White
        )
    ) {
        Text(text = "Login")
    }
}

@Composable
fun LoginDivider() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Divider(
            Modifier
                .height(1.dp)
                .weight(1f)
                .background(Color.Gray)
        )
        Text(
            text = "OR",
            modifier = Modifier.padding(horizontal = 18.dp),
            fontSize = 12.sp,
            color = Color.Gray
        )
        Divider(
            Modifier
                .height(1.dp)
                .weight(1f)
                .background(Color.Gray)
        )
    }
}

@Composable
fun SocialLogin(signInState: SignInState, onSignInClick: () -> Unit) {

    val context = LocalContext.current

    LaunchedEffect(key1 = signInState.signInError) {
        signInState.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(
            onClick = onSignInClick,
            shape = Shapes.medium
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Google Login",
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Iniciar con Google",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun Bottom(modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Divider(
            Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.size(24.dp))
        SignUp()
        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
fun SignUp() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = "Don't have an account?",
            color = Color.Gray
        )
        Text(
            text = "Sign Up.",
            Modifier
                .padding(horizontal = 8.dp),
            color = Color.Blue,
            fontWeight = FontWeight.Bold
        )
    }
}