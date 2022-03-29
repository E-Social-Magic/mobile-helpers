package com.example.helpers

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.helpers.ui.components.BottomNavController
import com.example.helpers.ui.components.SnackBarController
import com.example.helpers.ui.screens.ChatScreen
import com.example.helpers.ui.screens.NavGraphs
import com.example.helpers.ui.screens.destinations.*
import com.example.helpers.ui.screens.featureGroup.TopicListScreen
import com.example.helpers.ui.screens.featureLogin.LoginScreen
import com.example.helpers.ui.screens.featureLogin.LoginViewModel
import com.example.helpers.ui.screens.featurePayment.BalanceCoin
import com.example.helpers.ui.screens.featurePost.PostScreen
import com.example.helpers.ui.screens.featurePost.PostViewModel
import com.example.helpers.ui.screens.featureProfile.ProfileScreen
import com.example.helpers.ui.screens.featureProfile.UserViewModel
import com.example.helpers.ui.screens.featureVideo.VideosScreen
import com.example.helpers.ui.theme.EsocialTheme
import com.example.helpers.util.SessionManager
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.manualcomposablecalls.resultRecipient
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.offline.ChatDomain
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var client: ChatClient

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val loginViewModel: LoginViewModel = hiltViewModel()
            val userViewModel: UserViewModel = hiltViewModel()
            val isShowTopBar = loginViewModel.isShowTopBar.value
            val isShowBottomBar = loginViewModel.isShowBottomBar.value
            val scaffoldState = rememberScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            val snackBarController = SnackBarController(coroutineScope)
            var navController = rememberNavController()
            val postViewModel: PostViewModel = hiltViewModel()
            LaunchedEffect(key1 = loginViewModel.isLogin.value) {
                val user = loginViewModel.user.value
                if (user != null) {
                    ChatDomain.Builder(client, appContext = applicationContext).build()
                    val userChat = User().apply {
                        id = user.id
                        name = user.username
                        image = user.avatar.toString()
                    }
                    client.connectUser(user = userChat, token = sessionManager.fetchAuthToken()!!)
                        .enqueue()
                }
            }


            EsocialTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        scaffoldState = scaffoldState,
                        snackbarHost = { scaffoldState.snackbarHostState },
                        content = {
                            DestinationsNavHost(
                                navGraph = NavGraphs.root,
                                navController = navController,

                                ) {

                                composable(PostScreenDestination) {
                                    PostScreen(
                                        navigator = destinationsNavigator,
                                        loginViewModel = loginViewModel,
                                        changeBarState = { bar ->
                                            loginViewModel.isShowBottomBar.value = bar
                                            loginViewModel.isShowTopBar.value = bar
                                        },
                                        postViewModel = postViewModel,
                                        resultRecipient = resultRecipient(),
                                    )
                                }
                                composable(LoginScreenDestination) {
                                    LoginScreen(
                                        navigator = destinationsNavigator,
                                        scaffoldState = scaffoldState,
                                        coroutineScope = coroutineScope,
                                        snackBarController = snackBarController,
                                        loginViewModel = loginViewModel
                                    )
                                }
                                composable(ProfileScreenDestination) {
                                    ProfileScreen(
                                        navigator = destinationsNavigator,
                                        scaffoldState = scaffoldState,
                                        coroutineScope = coroutineScope,
                                        snackBarController = snackBarController,
                                        loginViewModel = loginViewModel,
                                        userViewModel = userViewModel,
                                    )
                                }
                                composable(VideosScreenDestination) {
                                    VideosScreen(
                                        navigator = destinationsNavigator,
                                        scaffoldState = scaffoldState,
                                        coroutineScope = coroutineScope,
                                        snackBarController = snackBarController,
                                    )
                                }
                                composable(BalanceCoinDestination) {
                                    BalanceCoin(
                                        navigator = destinationsNavigator,
                                        loginViewModel = loginViewModel,
                                    )
                                }
                                composable(ChatScreenDestination) {
                                    ChatScreen(
                                        navigator = destinationsNavigator,
                                        loginViewModel = loginViewModel,
                                        changeBarState = { bar ->
                                            loginViewModel.isShowTopBar.value = bar
                                        },
                                        onItemClick = { channel ->
                                            startActivity(
                                                MessagesActivity.createIntent(
                                                    this@MainActivity,
                                                    channelId = channel.cid
                                                )
                                            )
                                        },
                                        onBackPressed = { finish() }
                                    )
                                }
                                composable(TopicListScreenDestination) {
                                    TopicListScreen(
                                        navigator = destinationsNavigator,
                                        loginViewModel = loginViewModel
                                    )
                                }

                            }
                        },
                        bottomBar = { if (isShowBottomBar) BottomNavController(navController = navController) },
                    )
                }
            }
        }
    }


}



