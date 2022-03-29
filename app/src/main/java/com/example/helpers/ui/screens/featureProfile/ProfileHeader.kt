package com.example.helpers.ui.screens.featureProfile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helpers.R
import com.example.helpers.ui.screens.destinations.BalanceCoinDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun ProfileHeader(
    navigator:DestinationsNavigator,
    countHelped: String,
    countPosts: Int,
    coins:String,
) {
        Row(
            Modifier.height(64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VerticalDivider()
            Row(Modifier
                .weight(1f)
                .clickable { }
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(painter = painterResource(id = R.drawable.outline_check_circle_24), contentDescription = "", tint = Color(0xFF6FCF97))
                Column(Modifier.padding(8.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = countHelped, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "Đã giúp", color = Color.Gray, fontSize = 12.sp)
                }
            }

            VerticalDivider()
            Row(Modifier
                .fillMaxHeight()
                .weight(1f)
                .clickable {navigator.navigate(BalanceCoinDestination) }
                .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_coin), contentDescription = "", tint = Color(0xFFFF9900))
                Column(Modifier.padding(8.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = coins, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "Số dư", color = Color.Gray, fontSize = 12.sp)
                }
            }
            VerticalDivider()
            Row(Modifier
                .fillMaxHeight()
                .weight(1f)
                .clickable { }
                .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(painter = painterResource(id = R.drawable.outline_assignment_24), contentDescription = "", tint = MaterialTheme.colors.primary)
                Column(Modifier.padding(8.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = countPosts.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "Bài đăng", color = Color.Gray, fontSize = 12.sp)
                }
            }
    }
}
@Composable
fun VerticalDivider() {
    Divider(
        color = Color(0xFFF1F1F1),
        modifier = Modifier
            .width(1.dp)
            .height(32.dp)
    )
}
@Composable
fun HorizontalDivider() {
    Divider(
        color = Color(0xFFF1F1F1),
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
    )
}