package com.example.stocky.login

import android.app.Activity
import android.util.Patterns
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.stocky.model.Routes.HomeScreen
import com.example.stocky.ui.theme.Shapes

private const val CLOSE_ICON = "Close Icon"

@Composable
fun LoginScreen(navController: NavHostController) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Header(Modifier.align(Alignment.TopEnd))
        Body(Modifier.align(Alignment.Center))
        Bottom(Modifier.align(Alignment.BottomCenter), navController)
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
fun Body(modifier: Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isLoginEnabled by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        Logo(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.size(16.dp))
        Email(email) {
            email = it
            isLoginEnabled = enableLogin(email, password)
        }
        Spacer(modifier = Modifier.size(16.dp))
        Password(password) {
            password = it
            isLoginEnabled = enableLogin(email, password)
        }
        Spacer(modifier = Modifier.size(16.dp))
        ForgotPassword(Modifier.align(Alignment.End))
        Spacer(modifier = Modifier.size(16.dp))
        LoginButton(isLoginEnabled)
        Spacer(modifier = Modifier.size(16.dp))
        LoginDivider()
        Spacer(modifier = Modifier.size(16.dp))
        SocialLogin()
    }
}

@Composable
fun Logo(modifier: Modifier) {
    Text(
        text = "STOCKY",
        fontSize = 32.sp,
        color = Color.Gray,
        modifier = modifier
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
fun Password(password: String, onPasswordChanged: (String) -> Unit) {
    var passwordVisibility by remember { mutableStateOf(false) }
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
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
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
fun SocialLogin() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Image(
            imageVector = Icons.Default.Email,
            contentDescription = "Google Login",
            modifier = Modifier.padding(end = 16.dp),
            colorFilter = ColorFilter.tint(Color.DarkGray)
        )
        Text(
            text = "Iniciar con Google",
            color = Color.Gray
        )
    }
}

@Composable
fun Bottom(modifier: Modifier, navController: NavHostController) {
    Column(modifier = modifier.fillMaxWidth()) {
        Divider(
            Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.size(24.dp))
        SignUp(navController)
        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
fun SignUp(navController: NavHostController) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = "Don't have an account?",
            color = Color.Gray
        )
        Text(
            text = "Sign Up.",
            Modifier.padding(horizontal = 8.dp).clickable { navController.navigate(HomeScreen.route) },
            color = Color.Blue,
            fontWeight = FontWeight.Bold
        )
    }
}

fun enableLogin(email: String, password: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 6
}