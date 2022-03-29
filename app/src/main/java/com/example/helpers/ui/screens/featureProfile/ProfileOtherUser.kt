package com.example.helpers.ui.screens.featureProfile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.helpers.R
import com.example.helpers.ui.components.CircularProgressBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun ProfileOtherUserScreen(
    navigator: DestinationsNavigator,
    userViewModel: UserViewModel = hiltViewModel(),
    userId: String
) {
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    var user = userViewModel.user
    var posts = userViewModel.posts
    var helped = userViewModel.helped
    var isLoading = userViewModel.isLoading
    var coinsConvert: String by remember { mutableStateOf("") }
    coinsConvert = if (user.value!!.coins > 1000) {
        (user.value!!.coins / 1000).toString() + "k"
    } else {
        user.value!!.coins.toString()
    }
    LaunchedEffect(key1 = true) {
        userViewModel.findUserById(id = userId)
    }
    if (isLoading.value) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressBar(isDisplay = isLoading.value)
        }
    } else {
        Scaffold(topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .shadow(elevation = 2.dp)
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                IconButton(
                    onClick = { navigator.navigateUp()},
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back_arrow),
                        contentDescription = null
                    )
                }
                Text(
                    text = "Hồ sơ",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.15.sp
                    ),
                )
            }
        }) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(4.dp))
                ProfileSection(
                    navigator =navigator,
                    userName = user.value!!.userName,
                    description = user.value?.description,
                    avatar = user.value!!.avatar,
                    coins = coinsConvert,
                    posts = posts.value,
                    helped = helped.value,
                    group = user.value!!.subjects?.size.toString(),
                    follower = user.value!!.follower?.size.toString(),
                    following = user.value!!.following?.size.toString(),
                )
                Spacer(modifier = Modifier.height(25.dp))
                Spacer(modifier = Modifier.height(25.dp))
                Spacer(modifier = Modifier.height(10.dp))

            }
        }
    }
}