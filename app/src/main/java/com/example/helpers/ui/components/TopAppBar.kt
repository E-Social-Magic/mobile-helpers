package com.example.helpers.ui.components

//import coil.compose.rememberAsyncImagePainter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.helpers.R
import com.example.helpers.ui.screens.UserViewModel
import com.example.helpers.ui.screens.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Composable
fun TopApp(
    navigator: DestinationsNavigator,
    userViewModel: UserViewModel = hiltViewModel(),
    title: String,
    icon: ImageVector,
    scrollUpState: State<Boolean?>,
    onIconClick: () -> Unit
) {
    val searchBarState = userViewModel.searchBarState.value
    val searchValue = userViewModel.searchValue.value
    val isLoading = userViewModel.isLoading.value
    val user = userViewModel.user.value
    val position by animateFloatAsState(if (scrollUpState.value == true) -150f else 0f)

    LaunchedEffect(key1 = true) {
        userViewModel.getUserInfo()
    }
    var avatar by remember {
        mutableStateOf("https://gaplo.tech/content/images/2020/03/android-jetpack.jpg")
    }
    if (!isLoading && user?.avatar!= null)
        avatar = user.avatar
    if (position!=-150f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp)
            .graphicsLayer { translationY = (position)}
            .background(color = MaterialTheme.colors.primarySurface)
            .shadow(elevation = 1.dp)
            .padding(horizontal = 16.dp)
            .clickable { }
    ) {
        if (searchBarState)
            SearchBar(
                hint = "Tìm kiếm... ",
                searchValue = searchValue,
                onSearchValueChange = { userViewModel.onSearchChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onSearchBarStateChange = { userViewModel.onSearchBarStateChange(it) }
            ) {

            }
        else {
            Text(
                text = title,
                color = White,
                modifier = Modifier
                    .weight(0.5f)
                    .align(Alignment.CenterVertically),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.15.sp
                ),
            )
            Image(
                imageVector = icon,
                contentDescription = "Search Icon",
                colorFilter = ColorFilter.tint(White),
                modifier = Modifier
                    .clickable(onClick = {
                        onIconClick.invoke()
                        userViewModel.onSearchBarStateChange(true)
                    })
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
            )
//            Image(
//                imageVector = Icons.Outlined.Notifications,
//                contentDescription = "Notifications Icon",
//                colorFilter = ColorFilter.tint(White),
//                modifier = Modifier
//                    .clickable(onClick = onIconClick)
//                    .padding(16.dp)
//                    .align(Alignment.CenterVertically)
//            )
            Image(
                painter = rememberImagePainter(
                    data = avatar,
                    builder = {
                        crossfade(true)
                        placeholder(R.drawable.placeholder_image)
                        error(R.drawable.default_avatar)
                        transformations(CircleCropTransformation())
                        size(Int.MAX_VALUE)
                    }),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        navigator.navigate(ProfileScreenDestination)
                    }
            )
        }
    }

}