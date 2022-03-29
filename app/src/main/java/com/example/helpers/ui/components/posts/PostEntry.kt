package com.example.helpers.ui.components.posts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.helpers.models.domain.model.PostEntry
import com.example.helpers.ui.screens.destinations.PostDetailDestination
import com.example.helpers.ui.screens.featurePost.PostViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun PostEntry(
    post: PostEntry,
    changeBarState:(Boolean)->Unit,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    postViewModel: PostViewModel,
    avatar: String
) {
    var shouldShowComment by remember { mutableStateOf(false) }
    val isEditable = postViewModel.userIdSesstion == post.userId
    var isExpand by remember {
        mutableStateOf(false)
    }
    val isSovle = post.comments.find { it.isCorrect }?.isCorrect ?: false
    Card(
        shape = RoundedCornerShape(8.dp), elevation = 6.dp, modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(top = 8.dp)
    ) {
        Column {
            Row(modifier = Modifier.clickable { isExpand=!isExpand }) {
                HeaderPost(
                    navigator = navigator,
                    authorAvatar = post.authorAvatar,
                    userName = post.userName,
                    createdAt = post.createdAt,
                    userId = post.userId,
                    coins = post.coins,
                    costs = post.costs,
                    isSolve = isSovle,
                    hideName = post.hideName
                )
            }
            if (isExpand){
                Row(modifier = Modifier.clickable {
                    changeBarState.invoke(false)
                    navigator.navigate(PostDetailDestination(postId = post.id))
                }) {
                    if (post.images.isNotEmpty())
                        Card(modifier = Modifier
                            .weight(1f)
                            .padding(8.dp), shape = RoundedCornerShape(8.dp), elevation = 6.dp,) {
                            ImageContent(url =  post.images[0])
                        }
                    ContentPost(
                        modifier = Modifier.weight(1f),
                        postEntry= post
                    )

                }
            }

        }
    }
}