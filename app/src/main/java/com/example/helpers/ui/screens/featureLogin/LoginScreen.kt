package com.example.helpers.ui.screens.featureLogin

//import com.example.e_social.ui.theme.LogInButtonColor
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.helpers.ui.components.CircularProgressBar
import com.example.helpers.ui.components.DefaultSnackbar
import com.example.helpers.ui.components.SnackBarController
import com.example.helpers.ui.components.TextLogoApp
import com.example.helpers.ui.screens.destinations.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Destination(start =true)
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    loginViewModel: LoginViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    snackBarController: SnackBarController
) {
    val email = loginViewModel.email.value
    val password = loginViewModel.password.value
    val focusManager = LocalFocusManager.current
    val isLoading = loginViewModel.isLoading.value
    val isLogin = loginViewModel.isLogin.value
    LaunchedEffect(key1 = isLogin) {
        if (isLogin)
            navigator.navigate(PostScreenDestination)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .clickable { focusManager.clearFocus() }
    ) {
            Row(Modifier.weight(3f)) {
                TextLogoApp()
            }
        Column(
            Modifier.weight(6f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LoginFields(
                email,
                password,
                onEmailChange = { loginViewModel.onEmailChange(it) },
                onPasswordChange = { loginViewModel.onPasswordChange(it) }
            )
            Spacer(modifier = Modifier.height(60.dp))

            ButtonLogin(
                showMessage = {
                    snackBarController.getScope().launch {
                        snackBarController.showSnackbar(
                            snackbarHostState = scaffoldState.snackbarHostState,
                            message = "Lỗi",
                            actionLabel = "ẩn"
                        )
                    }
                },
                isLoading = isLoading
            ) {
                coroutineScope.launch {
                    loginViewModel.login()
                    if (loginViewModel.errorMessage.value.isEmpty()) {
                        navigator.navigate(ProfileScreenDestination)
                        loginViewModel.isShowBottomBar.value=true
                        loginViewModel.isShowTopBar.value=true

                    } else {
                        snackBarController.getScope().launch {
                            snackBarController.showSnackbar(
                                snackbarHostState = scaffoldState.snackbarHostState,
                                message = loginViewModel.errorMessage.value[0],
                                actionLabel = "ẩn"
                            )
                            loginViewModel.errorMessage.value = listOf()
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

//            CreateAccountButton {
//                navigator.navigate(SignUpScreenDestination())
//            }
        }

//        Row(Modifier.weight(1f)) {
//            ForgotPassword {
//                navigator.navigate(ForgotPasswordScreenDestination)
//            }
//        }
        DefaultSnackbar(
            snackbarHostState = scaffoldState.snackbarHostState,
            onDismiss = {
                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
            }
        )

    }
//    }

}


@Composable
fun LoginFields(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,

    ) {
    var passwordVisualTransformation by remember {
        mutableStateOf(true)
    }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            placeholder = { Text(text = "user@email.com") },
            label = { Text(text = "Email") },
            onValueChange = onEmailChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),

            shape = RoundedCornerShape(24)
        )

        OutlinedTextField(
            value = password,
            placeholder = { Text(text = "Mật khẩu") },
            label = { Text(text = "Mật khẩu ") },
            onValueChange = onPasswordChange,
            visualTransformation = if (passwordVisualTransformation) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            shape = RoundedCornerShape(24),
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisualTransformation = !passwordVisualTransformation
                }) {
                    Icon(
                        imageVector = if (passwordVisualTransformation) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "visibility",
                        tint = Color.Black
                    )
                }
            }
        )
    }
}

@Composable
fun ButtonLogin(
    modifier: Modifier = Modifier,
    showMessage: () -> Unit,
    isLoading: Boolean,
    onLoginPress: () -> Unit
) {
    Button(
        onClick = {
//            if()
            onLoginPress.invoke()
        },
        modifier = modifier
            .width(197.dp)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        shape = RoundedCornerShape(25),
        enabled = !isLoading
    ) {
        if (isLoading)
            CircularProgressBar(isDisplay = isLoading)
        else
            Text(
                "Đăng nhập",
                fontSize = 17.sp,
            )
    }
}

@Composable
fun CreateAccountButton(modifier: Modifier = Modifier, onCreateAccountClick: () -> Unit) {
    OutlinedButton(
        onClick = { onCreateAccountClick.invoke() },
        modifier = modifier
            .width(197.dp)
            .height(48.dp),
        shape = RoundedCornerShape(25),
    ) {
        Text(
            "Tạo tài khoản",
            fontSize = 17.sp
        )
    }
}

@Composable
fun ForgotPassword(onForgotPasswordClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxHeight()) {
        ClickableText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 14.sp)) {
                    append("Quên mật khẩu ? ")
                }

            },
            style = MaterialTheme.typography.caption,
            modifier =
            Modifier
                .padding(bottom = 36.dp)
                .align(Alignment.BottomCenter),
            onClick = { onForgotPasswordClick.invoke() }
        )

    }
}


