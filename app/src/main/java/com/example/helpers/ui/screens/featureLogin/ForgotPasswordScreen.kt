package com.example.helpers.ui.screens.featureLogin

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ForgotPasswordScreen(
    navigator: DestinationsNavigator,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val email = loginViewModel.email.value
    VerifyInput(
        navigator = navigator,
        email = email,
        onEmailChange = { loginViewModel.onEmailChange(it) })
}