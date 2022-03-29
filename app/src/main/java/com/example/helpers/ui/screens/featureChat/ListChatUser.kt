package com.example.helpers.ui.screens.featureChat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helpers.R
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.helpers.MessagesActivity
import com.example.helpers.ui.components.SearchBar
import com.example.helpers.util.TimeConverter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import java.util.*

@Destination
@Composable
fun ListChatUser(
    navigator: DestinationsNavigator,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userList = chatViewModel.userList.value
    val coroutineScope = rememberCoroutineScope()
    Scaffold(topBar = {
        TopApp(
            title = "Users",
            icon = Icons.Default.Search,
            onIconClick = { navigator.navigateUp() })
    }) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(bottom = 50.dp)
        ) {
            items(userList.size) { index ->
                val user = userList[index]
                Row(modifier = Modifier
                    .clickable {
                        coroutineScope.launch {
                            chatViewModel.createNewChannel(
                                user.id,
                                action = { cid ->
                                    context.startActivity(
                                        MessagesActivity.createIntent(
                                            context,
                                            channelId = cid
                                        )
                                    )
                                })
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically)

                {
                    Image(
                        painter = rememberImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = user.image)
                                .error(R.drawable.default_avatar)
                                .apply(block = fun ImageRequest.Builder.() {
                                    transformations(CircleCropTransformation())
                                }).build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, MaterialTheme.colors.secondaryVariant, CircleShape),

                        )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user.name,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                        Text(
                            text = TimeConverter.convertDate(user.lastActive?.time ?: Date().time),
                            color = Color.Gray
                        )
                    }
                }
            }
        }

    }

}

@Composable
private fun TopApp(
    chatViewModel: ChatViewModel = hiltViewModel(),
    title: String,
    icon: ImageVector,
    onIconClick: () -> Unit
) {
    val searchBarState = chatViewModel.searchBarState.value
    val searchValue = chatViewModel.searchValue.value
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp)
            .background(color = Color.White)
            .shadow(elevation = 1.dp)
            .clickable { }
    ) {
        if (searchBarState)
            SearchBar(
                hint = "Search...",
                searchValue = searchValue,
                onSearchValueChange = { chatViewModel.onSearchChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onSearchBarStateChange = { chatViewModel.onSearchBarStateChange(it) }
            ) {

            }
        else {
            IconButton(
                onClick = onIconClick,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = null
                )
            }
            Text(
                text = title,
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
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier
                    .clickable(onClick = {
                        chatViewModel.onSearchBarStateChange(true)
                    })
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}