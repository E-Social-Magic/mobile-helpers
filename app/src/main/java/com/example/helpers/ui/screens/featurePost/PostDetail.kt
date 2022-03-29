package com.example.helpers.ui.screens.featurePost

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.helpers.models.domain.model.PostEntry
import com.example.helpers.ui.components.CircularProgressBar
import com.example.helpers.ui.components.SimpleTopAppBar
import com.example.helpers.ui.components.posts.*
import com.example.helpers.ui.screens.featureLogin.LoginViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(route = "post_detail")
@Composable
fun PostDetail(
    navigator: DestinationsNavigator,
    postViewModel: PostViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    postId: String
) {
    val avatar = loginViewModel.user.value?.avatar ?: ""
    var shouldShowComment by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }
    var isEditable = false
    val post = postViewModel.post.value
    if (post != null)
        isEditable = postViewModel.userIdSesstion == post!!.userId
    LaunchedEffect(key1 = true) {
        loginViewModel.isShowBottomBar.value=false
         postViewModel.findPostById(id = postId)
        isLoading = false
    }
    if (isLoading) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressBar(isDisplay = isLoading)
        }
    } else
        Column(Modifier.verticalScroll(rememberScrollState()).padding(bottom = 50.dp)) {
            SimpleTopAppBar(title = "Chi tiết bài viết") {
                navigator.navigateUp()
            }
            when (post) {
                null -> {
                    Text(
                        text = "Không tìm thấy bài viết này",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxSize()
                    )

                }
                else -> {
                    Card(
                        shape = RoundedCornerShape(8.dp), elevation = 6.dp, modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp)
                            .padding(top = 8.dp)
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            HeaderPost(
                                navigator = navigator,
                                userId = post!!.userId,
                                authorAvatar = post!!.authorAvatar,
                                userName = post!!.userName,
                                createdAt = post!!.createdAt,
                                coins = post!!.coins,
                                costs = post!!.costs
                            )
                            TitlePost(post!!.title)
                            TextContent(post!!.content)
                            post!!.images.map { image ->
                                ImageContent(url = image)
                            }
                            BottomPostAction(
                                post!!,
                                postViewModel = postViewModel,
                                onCommentIconClick = { shouldShowComment = !shouldShowComment },
                            )
                        }
                    }
                    if (shouldShowComment) {
                        post.let {
                            CommentInput(
                                hint = "comment something",
                                postId = it.id,
                                onSubmit = { postId, comment ->
                                    postViewModel.submitComment(
                                        postId = post!!.id,
                                        message = comment,
                                    )

                                },
                                avatar = avatar
                            )
                            ListComment(
                                postViewModel = postViewModel,
                                messages = post!!.comments,
                                isEditable = isEditable,
                                postId = post!!.id,
                                onwerPostId = post!!.userId
                            )

                        }
                    }
                }
            }


        }

}