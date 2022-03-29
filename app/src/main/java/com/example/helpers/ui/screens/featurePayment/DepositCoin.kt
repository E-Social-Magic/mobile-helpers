package com.example.helpers.ui.screens.featurePayment

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.helpers.ui.components.SimpleTopAppBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.text.DecimalFormat
import java.text.NumberFormat

@Destination
@Composable
fun DepositCoins(
    navigator: DestinationsNavigator,
    paymentViewModel: PaymentViewModel = hiltViewModel()
) {
    val formatter: NumberFormat = DecimalFormat("#,###")
    val coins = paymentViewModel.coins
    val assumeBalance = paymentViewModel.assumeBalance.value
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Surface {
        Column {
            SimpleTopAppBar(title = "Nạp tiền", onIconBackClick = { navigator.navigateUp() })
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.weight(5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = formatter.format(assumeBalance),
                    onValueChange = {
                        val number = it.replace(",", "").replace(".", "").toIntOrNull()
                        if (number != null && number < 20000000) {
                            if (number<10000)
                                Toast.makeText(context,"Số tiền phải lớn hơn 10,000 vnđ",Toast.LENGTH_SHORT).show()
                            else
                                paymentViewModel.onCoinChange(number.toLong())

                        }
                    },
                    placeholder = { Text(text = "Nhập tiền bạn muốn nạp") },
                    label = { Text(text = "Số dư hiện tại $coins - số dư sau khi nạp ${assumeBalance + coins} ") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        paymentViewModel.depositCoins(action = {
                            val i = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                            context.startActivity(i)
                        })
                    },
                        shape = RoundedCornerShape(24)
                    ) {
                        Text(text = "Nạp", fontSize = 20.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(horizontal = 10.dp))
                    }
                }
            }
        }
    }
}