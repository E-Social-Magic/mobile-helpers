package com.example.helpers.ui.screens.featurePayment

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helpers.ui.components.SimpleTopAppBar
import com.example.helpers.ui.screens.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun  SuccessPayment(navigator: DestinationsNavigator){
    Box {
        Column(modifier = Modifier.align(Alignment.TopStart)) {
            SimpleTopAppBar(title = "Rút tiền ", onIconBackClick = { navigator.navigate(ProfileScreenDestination) })
            Spacer(modifier = Modifier.weight(1f))
        }
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Done, contentDescription = null, tint = Color.Green, modifier = Modifier.size(50.dp))
            Text(
                text = "Tạo đơn thành công, vui lòng đợi yêu cầu rút tiền được phê duyệt",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

        }
    }

}