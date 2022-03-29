package com.example.helpers.ui.screens.featureGroup


//import coil.compose.AsyncImage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.helpers.models.domain.model.TopicIndexListEntry
import com.example.helpers.ui.components.NextStepButton
import com.example.helpers.ui.screens.destinations.PostScreenDestination
import com.example.helpers.ui.screens.featureLogin.LoginViewModel
import com.example.helpers.ui.theme.Grey100
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable
fun TopicListScreen(
    navigator: DestinationsNavigator,
    loginViewModel: LoginViewModel = hiltViewModel(),
    topicListViewModel: TopicListViewModel = hiltViewModel(),
) {

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        val sliderPosition = loginViewModel.sliderValue.value
        val steps = loginViewModel.steps.value
        val listTopicSelected = mutableListOf<String>()
        loginViewModel.isShowBottomBar.value=false
        Column {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Chọn những chủ đề mà bạn yêu thích",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
//                SliderBar(sliderPosition=sliderPosition,steps=steps,onChange={loginViewModel.onChangSlider(it)})
            }
            Row(modifier = Modifier.weight(1f)) {
                ComposeMenu(
                    loginViewModel.levels,
                    onSelectLevel = { levelSelected -> loginViewModel.level.value = levelSelected })
            }
            Row(modifier = Modifier.weight(7f)) {
                Spacer(modifier = Modifier.height(10.dp))
                TopicList(navigator = navigator,topicListViewModel)
                Spacer(modifier = Modifier.height(10.dp))
            }
            Row(
                modifier = Modifier
                    .weight(1.5f)
                    .padding(10.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                NextStepButton(
                    enabled = topicListViewModel.selectedTopic.value.isNotEmpty(),
                    onButtonClick = {
                        loginViewModel.finishSignUp(topicListViewModel.selectedTopic.value)
                        navigator.navigate(PostScreenDestination)
                        loginViewModel.isLogin.value = true
                        loginViewModel.isShowTopBar.value = true
                        loginViewModel.isShowBottomBar.value = true
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = "Tiếp theo",
                        fontSize = 15.sp,
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(Icons.Outlined.ArrowForward, contentDescription = "ssdsadsa")
                }
            }
        }
    }
}


@Composable
fun TopicList(
    navigator: DestinationsNavigator,
    viewModel: TopicListViewModel = hiltViewModel(),
) {
    val topicList by remember { viewModel.topicList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val itemCount = if (topicList.size % 3 == 0) {
            topicList.size / 3
        } else {
            topicList.size / 3
        }
        items(itemCount) {
            if (it >= itemCount - 2 && !endReached) {
                viewModel.loadTopicPaginated()
            }
            TopicIndexRow(
                rowIndex = it,
                entries = topicList,
                navigator = navigator,
                selectedTopic = { idSelected -> viewModel.selectedTopics(idSelected) })
        }
    }

    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadTopicPaginated()
            }
        }
    }

}

@Composable
fun TopicIndexEntry(
    entry: TopicIndexListEntry,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: TopicListViewModel = hiltViewModel(),
    selectedTopic: (String) -> Unit
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black),
        startY = sizeImage.height.toFloat() / 3f,
        endY = sizeImage.height.toFloat()
    )
    val checkedState = remember { mutableStateOf(false) }
    Box(
        contentAlignment = Center,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
            .shadow(elevation = 8.dp, RoundedCornerShape(10.dp))
            .clickable {
            }
    ) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(gradient)
        )
        Image(
            painter = rememberImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = entry.avatar)
                    .apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                        size(Int.MAX_VALUE)
                    }).build()
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().onGloballyPositioned {
                sizeImage = it.size
            }
        )
        Checkbox(
            checked = checkedState.value,
            modifier = Modifier.align(BottomEnd),
            onCheckedChange = {
                checkedState.value = it
                selectedTopic.invoke(entry.id)
            },
            colors = CheckboxDefaults.colors(
                uncheckedColor = Color.White,
                checkmarkColor = MaterialTheme.colors.primary,
                checkedColor = Color.White
            )
        )
        Text(
            text = entry.groupName,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )

    }
}

@Composable
fun TopicIndexRow(
    rowIndex: Int,
    entries: List<TopicIndexListEntry>,
    navigator: DestinationsNavigator,
    selectedTopic: (String) -> Unit
) {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TopicIndexEntry(
                entry = entries[rowIndex * 3],
                navigator = navigator,
                modifier = Modifier.weight(1f),
                selectedTopic = selectedTopic
            )
            if (entries.size >= rowIndex * 3 + 2) {
                TopicIndexEntry(
                    entry = entries[rowIndex * 3 + 1],
                    navigator = navigator,
                    modifier = Modifier.weight(1f),
                    selectedTopic = selectedTopic
                )
                TopicIndexEntry(
                    entry = entries[rowIndex * 3 + 2],
                    navigator = navigator,
                    modifier = Modifier.weight(1f),
                    selectedTopic = selectedTopic
                )
            }
        }
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Thử lại")
        }
    }
}


@Composable
fun ComposeMenu(levels: List<String>, onSelectLevel: (String) -> Unit) {
    var expanded = remember { mutableStateOf(false) }
    var selectedIndex = remember { mutableStateOf(0) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 20.dp)
        ) {
            Text(
                "Học vấn:",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Column {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        modifier = Modifier.width(120.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Grey100),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            expanded.value = true
                            onSelectLevel.invoke(levels[selectedIndex.value])
                        }, content = {
                            Text("Lớp ${levels[selectedIndex.value]}")
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)

                        })
                }
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    modifier = Modifier
                        .background(Grey100)
                        .shadow(elevation = 2.dp)
                        .width(120.dp),
                ) {
                    levels.forEachIndexed { index, s ->
                        DropdownMenuItem(onClick = {
                            selectedIndex.value = index
                            expanded.value = false
                        }) {
                            Text(text = s.toString())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SliderBar(sliderPosition: Float, steps: Float, onChange: (Float) -> Unit) {

    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Slider(value = sliderPosition, onValueChange = onChange, valueRange = 1f..2f, steps = 2)
    }
}
