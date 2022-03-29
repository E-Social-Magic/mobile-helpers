package com.example.helpers.ui.screens.featureGroup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.helpers.R
import com.example.helpers.models.data.response.Topic
import com.example.helpers.models.data.response.TopicList
import com.example.helpers.ui.components.CircularProgressBar
import com.example.helpers.ui.components.ImageBuilderCircle
import com.example.helpers.ui.screens.destinations.ChooseGroupScreenDestination
import com.example.helpers.ui.screens.destinations.TopicListScreenDestination
import com.example.helpers.ui.screens.featureProfile.HorizontalDivider
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlinx.coroutines.Job


@Destination
@Composable
fun ChooseGroupScreen(
    navigator: DestinationsNavigator,
    resultNavigator: ResultBackNavigator<String?>,
    viewModel: GroupViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val groups = viewModel.allGroups.value
    var searchedText by remember { mutableStateOf("") }
    var currentJob by remember { mutableStateOf<Job?>(null) }
    val activeColor = MaterialTheme.colors.onSurface
    val isLoading = viewModel.isLoading.value
    LaunchedEffect(Unit) {
        viewModel.searchGroups(searchedText)
    }


    Box(Modifier.fillMaxSize()) {
        Column(Modifier.align(Alignment.TopStart)) {
            ChooseGroupTopBar(navigator = navigator)
//        TextField(
//            value = searchedText,
//            onValueChange = {
//                searchedText = it
//                currentJob?.cancel()
//                currentJob = scope.async {
//                    delay(SEARCH_DELAY_MILLIS)
//                    viewModel.searchGroups(searchedText)
//                }
//            },
//            leadingIcon = {
//                Icon(Icons.Default.Search, contentDescription = stringResource(id = R.string.search))
//            },
//            label = { Text(stringResource(R.string.search)) },
//            modifier = modifier
//                .fillMaxWidth()
//                .padding(horizontal = 8.dp),
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                focusedBorderColor = activeColor,
//                focusedLabelColor = activeColor,
//                cursorColor = activeColor,
//                backgroundColor = MaterialTheme.colors.surface
//            )
//        )
            when (isLoading) {
                true -> {
                    Row(
                        Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressBar(isDisplay = isLoading)
                    }
                }
                false -> {
                    if (groups != null)
                        Searchedgroups(
                            navigator = navigator,
                            groups = groups,
                            modifier = modifier
                        ) {
                            viewModel.onSelectedGroup(it)
                            if (it != null) {
                                resultNavigator.navigateBack(result = it.id)
                            } else {
                                resultNavigator.navigateBack(result = null)
                            }
                        }
                }

            }
        }
        ClickableText(

            text = buildAnnotatedString {
                withStyle( style = SpanStyle(fontSize = 14.sp)) {
                    append("Muốn tham gia thêm nhóm ")
                    withStyle(
                        style = SpanStyle(
                            color = Color.Blue,
                            fontWeight = FontWeight.Bold
                        ),
                    ) {
                        append("tại đây")
                    }
                }
            },
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 70.dp),
            style = MaterialTheme.typography.caption,
            onClick = { navigator.navigate(TopicListScreenDestination) },
        )
    }
}



@Composable
fun Searchedgroups(
    navigator: DestinationsNavigator,
    groups: TopicList,
    modifier: Modifier = Modifier,
    onGroupClicked: (Topic?) -> Unit,

    ) {
    Groups(groups = groups, onGroupClicked = onGroupClicked)
}

@Composable
fun ChooseGroupTopBar(navigator: DestinationsNavigator, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colors
    TopAppBar(
        title = {
            Text(
                fontSize = 20.sp,
                text = "Chọn nhóm",
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { navigator.navigateUp() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.close)
                )
            }
        },
        backgroundColor = colors.primary,
        elevation = 0.dp,
        modifier = modifier
            .height(48.dp)
            .background(Color.Blue)
    )
}

@Composable
fun Groups(modifier: Modifier = Modifier, groups: TopicList, onGroupClicked: (Topic?) -> Unit) {

    Spacer(modifier = modifier.height(4.dp))
    Group(null, onGroupClicked = onGroupClicked)
    groups.groups.forEach {
        Group(it, onGroupClicked = onGroupClicked)
    }
}

@Composable
fun Group(group: Topic?, modifier: Modifier = Modifier, onGroupClicked: (Topic?) -> Unit = {}) {
    if (group == null) {
        Row(modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxWidth()
            .clickable { onGroupClicked.invoke(null) }
        ) {
            Text(
                fontSize = 20.sp,
                text = "Tất cả",
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .padding(vertical = 16.dp)
            )
        }
        HorizontalDivider()
    } else {
        Row(modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxWidth()
            .clickable { onGroupClicked.invoke(group) }
        ) {
            ImageBuilderCircle(50.dp, group.avatar)
            Text(
                fontSize = 20.sp,
                text = group.groupName,
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun BackgroundText(text: String) {
    Text(
        fontWeight = FontWeight.Medium,
        text = text,
        fontSize = 10.sp,
        color = Color.DarkGray,
        modifier = Modifier
            .background(color = MaterialTheme.colors.secondary)
            .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
    )
}

@Composable
fun GroupPicker(
    navigator: DestinationsNavigator,
    selectedGroup: Topic?,
    resultRecipient: ResultRecipient<ChooseGroupScreenDestination, String>
) {
    val selectedText = selectedGroup?.groupName ?: "Chọn nhóm"
    val avatar =
        selectedGroup?.avatar ?: "https://gaplo.tech/content/images/2020/03/android-jetpack.jpg"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(top = 16.dp)
            .clickable {
                navigator.navigate(ChooseGroupScreenDestination)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageBuilderCircle(size = 35.dp, url = avatar)
        Text(
            text = selectedText,
            modifier = Modifier.padding(start = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}