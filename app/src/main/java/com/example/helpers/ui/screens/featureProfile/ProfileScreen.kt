package com.example.helpers.ui.screens.featureProfile

//import coil.compose.rememberAsyncImagePainter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.helpers.R
import com.example.helpers.models.data.response.DataX
import com.example.helpers.ui.components.CircularProgressBar
import com.example.helpers.ui.components.SnackBarController
import com.example.helpers.ui.components.posts.ImageContent
import com.example.helpers.ui.components.posts.ImageContentGrid
import com.example.helpers.ui.screens.destinations.LoginScreenDestination
import com.example.helpers.ui.screens.featureLogin.LoginViewModel
import com.example.helpers.ui.theme.Grey100
import com.example.helpers.util.TimeConverter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    snackBarController: SnackBarController,
    loginViewModel: LoginViewModel,
    userViewModel: UserViewModel,

) {
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    loginViewModel.isShowBottomBar.value=true
    val paymentList = userViewModel.payment
    var user = userViewModel.user
    var images = userViewModel.images
    var posts = userViewModel.posts
    var helped = userViewModel.helped
    var isLoading = userViewModel.isLoading
    var coinsConvert : String by remember{ mutableStateOf("") }
    coinsConvert =   if(user.value!!.coins > 1000000)
        (user.value!!.coins/1000000).toString()+ "M" else
        if (user.value!!.coins > 10000)
            (user.value!!.coins/1000).toString()+ "k"
        else user.value!!.coins.toString()

    LaunchedEffect(key1 = true ){
        userViewModel.getUserInfo()
    }
    if (isLoading.value){
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            CircularProgressBar(isDisplay = isLoading.value)
        }
    } else{
        Scaffold(topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .shadow(elevation = 2.dp)
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hồ sơ",
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.Start),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.15.sp
                    ),
                )
                Button(
                    onClick = {
                        loginViewModel.isShowBottomBar.value=false
                        loginViewModel.logOut()
                        navigator.navigate(LoginScreenDestination) },
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.End), shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Đăng xuất")
                }

            }
        }) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp)) {
                Spacer(modifier = Modifier.height(4.dp))
                ProfileSection(
                    navigator=navigator,
                    userName = user.value!!.userName,
                    description = user.value?.description,
                    enableRedirectPayment=true,
                    avatar = user.value!!.avatar,
                    coins = coinsConvert,
                    posts = posts.value,
                    helped = helped.value,
                    group = user.value!!.subjects?.size.toString() ,
                    follower = user.value!!.follower?.size.toString(),
                    following = user.value!!.following?.size.toString(),
                )
                Spacer(modifier = Modifier.height(25.dp))
                Spacer(modifier = Modifier.height(25.dp))
                Spacer(modifier = Modifier.height(10.dp))

                PostTabView(
                    imageWithTexts = listOf(
                        ImageWithText(
                            image = painterResource(id = R.drawable.ic_grid),
                            text = "Posts"
                        ),
//                        ImageWithText(
//                            image = painterResource(id = R.drawable.ic_reels),
//                            text = "Reels"
//                        ),
                        ImageWithText(
                            image = painterResource(id = R.drawable.ic_igtv),
                            text = "IGTV"
                        ),
                    )
                ) {
                    selectedTabIndex = it
                }
                when (selectedTabIndex) {
                    0 -> PostSection(
                        posts = images.value,
                        modifier = Modifier.fillMaxSize()
                    )
                    1 -> ProfileTransactionHistory(
                        paymentList = paymentList.value,
                        avatar = user.value!!.avatar
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileSection(
    navigator:DestinationsNavigator,
    modifier: Modifier = Modifier,
    enableRedirectPayment:Boolean= false,
    userName:String,
    description: String?,
    avatar: String,
    coins: String,
    posts: Int,
    helped: Int,
    group: String,
    follower: String,
    following: String,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            RoundImage(
                image = rememberImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = avatar)
                        .error(R.drawable.default_avatar)
                        .apply(block = fun ImageRequest.Builder.() {
                            transformations(CircleCropTransformation())
                        }).build()
                ),
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Column(
                Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)) {
                Text(text = userName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            StatSection(
                navigator =navigator,
                helped = helped.toString(),
                enableRedirectPayment=enableRedirectPayment,
                posts = posts,
                coins = coins,
                group = group,
                follower = follower,
                following = following,
            )
        }
        HorizontalDivider()
        ProfileDescription(
            description = description,
        )
    }
}

@Composable
fun RoundImage(
    image: Painter,
    modifier: Modifier = Modifier
) {
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = CircleShape
            )
            .padding(3.dp)
            .clip(CircleShape)
    )
}

@Composable
fun StatSection(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    enableRedirectPayment:Boolean= false,
    helped: String,
    posts:Int,
    coins: String,
    group: String,
    follower: String,
    following: String,
) {
    Column(
        modifier = modifier
    ) {
        ProfileHeader(navigator =navigator ,helped, posts, coins,enableRedirectPayment=enableRedirectPayment)
        HorizontalDivider()
        Row(
            modifier = Modifier.height(64.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
                Row(
                    Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VerticalDivider()
                    Row(Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable { }
                        .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileStat(numberText = group, text = "Nhóm")
                    }

                    VerticalDivider()
                    Row(Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable { }
                        .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileStat(numberText = follower, text = "Follower")
                    }
                    VerticalDivider()
                    Row(Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable { }
                        .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileStat(numberText = following, text = "Đang Follow")

                    }
                }
        }

    }
}

@Composable
fun ProfileStat(
    numberText: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = numberText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = text, color = Color.Gray, fontSize = 12.sp)
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileTransactionHistory(
    paymentList: List<DataX>,
    avatar: String,
    modifier: Modifier = Modifier
) {
    if (paymentList.isEmpty()){
        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text(text = "Danh sách rỗng")
        }
    }
    LazyColumn {
        items(paymentList.size) { index->
            val payment = paymentList[index]
            val color = if(index%2 == 0) Color.White else Grey100
            val colorCoins = if(payment.type == "in") Color(0xFF41924B) else Color(0xFFCC0000)
                Row(modifier = Modifier
                    .background(color)
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Image(
                        painter = rememberImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = avatar)
                                .error(R.drawable.default_avatar)
                                .apply(block = fun ImageRequest.Builder.() {
                                    transformations(CircleCropTransformation())
                                }).build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, MaterialTheme.colors.secondaryVariant, CircleShape),

                        )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = payment.username,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Số tiền: " + if(payment.type == "in") "+ " else {"- "} + payment.amount.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = colorCoins
                            )
                            TimeConverter.getDateTime(payment.createdAt)?.let {
                                Text(
                                    text = it,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 13.sp,
                                    modifier = Modifier
                                        .align(Alignment.Bottom)
                                )
                            }
                        }
                        Text(
                            text = "Số dư ví: " + if(payment.resultCode == "7000") "?" else payment.accountBalance.toString(),
                            fontWeight = FontWeight.W500,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .align(Alignment.Start)
                        )
                        Text(
                            text = "Trạng thái giao dịch: " + payment.message,
                            fontWeight = FontWeight.Light,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .align(Alignment.Start)
                        )
                    }
                }
        }
    }
}

@Composable
fun ProfileDescription(
    description: String?,
) {
    val letterSpacing = 0.5.sp
    val lineHeight = 20.sp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Mô tả",
            fontWeight = FontWeight.Bold,
            letterSpacing = letterSpacing,
            lineHeight = lineHeight,
            textAlign = TextAlign.Center
        )
        if (description != null) {
            Text(
                text = description,
                letterSpacing = letterSpacing,
                lineHeight = lineHeight
            )
        }
    }
}

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(6.dp)
    ) {
        if (text != null) {
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}

@Composable
fun PostTabView(
    modifier: Modifier = Modifier,
    imageWithTexts: List<ImageWithText>,
    onTabSelected: (selectedIndex: Int) -> Unit
) {
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    val inactiveColor = Color(0xFF777777)
    TabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = Color.Transparent,
        contentColor = Color.Black,
        modifier = modifier
    ) {
        imageWithTexts.forEachIndexed { index, item ->
            Tab(
                selected = selectedTabIndex == index,
                selectedContentColor = Color.Black,
                unselectedContentColor = inactiveColor,
                onClick = {
                    selectedTabIndex = index
                    onTabSelected(index)
                }
            ) {
                Icon(
                    painter = item.image,
                    contentDescription = item.text,
                    tint = if (selectedTabIndex == index) Color.Black else inactiveColor,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(20.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostSection(
    posts: List<String>,
    modifier: Modifier = Modifier
) {
    if (posts.isEmpty()){
        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
           Text(text = "Danh sách rỗng")
        }
    }
    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        modifier = modifier
            .scale(1.01f)
    ) {
        items(posts.size) {index ->
            ImageContentGrid(
                url = posts[index]
            )
        }
    }
}

data class ImageWithText(
    val image: Painter,
    val text: String
)