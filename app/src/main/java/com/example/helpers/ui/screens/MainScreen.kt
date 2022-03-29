package com.example.helpers.ui.screens

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.helpers.ui.components.SnackBarController
import com.example.helpers.ui.screens.destinations.LoginScreenDestination
import com.example.helpers.ui.screens.featureLogin.LoginViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope

@Destination
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    snackBarController: SnackBarController,
    loginViewModel: LoginViewModel = hiltViewModel(),
    changeBarState:(Boolean)->Unit
) {
    LaunchedEffect(key1 = loginViewModel.isLogin.value ){
        if(!loginViewModel.isLogin.value){
            navigator.navigate(LoginScreenDestination)
        }
    }
//    PostScreen(navigator =navigator, changeBarState = changeBarState)
}








