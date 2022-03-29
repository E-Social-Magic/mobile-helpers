package com.example.helpers.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.helpers.ui.screens.destinations.ListChatUserDestination
import com.example.helpers.ui.screens.featureChat.ChatViewModel
import com.example.helpers.ui.screens.featureLogin.LoginViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme

@Destination
@Composable
fun ChatScreen (
    navigator: DestinationsNavigator,
    loginViewModel: LoginViewModel,
    changeBarState:(Boolean)->Unit,
    onItemClick:(Channel)->Unit,
    onBackPressed:()->Unit,
    chatViewModel: ChatViewModel= hiltViewModel()
) {

    LaunchedEffect(key1 = true ){
        changeBarState.invoke(false)
    }
    ChatTheme{
        ChannelsScreen(
            title = "Chat screen",
            isShowingSearch = true,
            isShowingHeader = true,
            onItemClick = onItemClick,
            onBackPressed =onBackPressed,
            onHeaderActionClick = {
                    navigator.navigate(ListChatUserDestination)
            }
            )
    }
}