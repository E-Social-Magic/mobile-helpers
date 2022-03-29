package com.example.helpers.ui.screens.featurePayment

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.helpers.ui.components.SimpleTopAppBar
import com.example.helpers.ui.screens.destinations.SuccessPaymentDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.text.DecimalFormat
import java.text.NumberFormat

@Destination
@Composable
fun WithDraw(
    navigator: DestinationsNavigator, paymentViewModel: PaymentViewModel = hiltViewModel()
) {
    val formatter: NumberFormat = DecimalFormat("#,###")
    val coins = paymentViewModel.coins
    val assumeBalance = paymentViewModel.assumeBalance.value
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val phone = paymentViewModel.phone.value
    val name = paymentViewModel.name.value
    val isEnable = remember{ mutableStateOf(true)}
    Surface {
        Column {
            SimpleTopAppBar(title = "Rút tiền ", onIconBackClick = { navigator.navigateUp() })
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.weight(5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = name,
                    placeholder = { Text(text = "Tên chủ tài khoản") },
                    label = { Text(text = "Tên chủ tài khoản") },
                    onValueChange = { paymentViewModel.onNameChange(it) },
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    shape = RoundedCornerShape(24),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                OutlinedTextField(
                    value = phone,
                    placeholder = { Text(text = "Eg:098654321") },
                    label = { Text(text = "Số điện thoại") },
                    onValueChange = {
                        if (it.isDigitsOnly() && it.startsWith("0"))
                            paymentViewModel.onPhoneChange(it)
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Phone
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    shape = RoundedCornerShape(24),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                OutlinedTextField(
                    value = formatter.format(assumeBalance),
                    onValueChange = {
                        val number = it.replace(",", "").replace(".", "").toIntOrNull()
                        if (number != null && number < 20000000)
                            if (number < 10000)
                                Toast.makeText(
                                    context,
                                    "Số tiền phải lớn hơn 10,000 vnđ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            else
                                paymentViewModel.onCoinChange(number.toLong())
                    },
                    placeholder = { Text(text = "Nhập tiền bạn muốn rút") },
                    label = { Text(text = "Số dư hiện tại $coins") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                    shape = RoundedCornerShape(24),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(enabled = phone.length == 10 && name.isNotEmpty()&&isEnable.value, onClick = {
                        if (assumeBalance > coins) {
                            Toast.makeText(context, "Số dư không đủ", Toast.LENGTH_SHORT).show()
                        } else {
                            isEnable.value=false
                            paymentViewModel.withDraw(action = {
                                if (it)
                               navigator.navigate(SuccessPaymentDestination)
                                else
                                    Toast.makeText(
                                        context,
                                        "Đã có lỗi xảy ra vui Long kiểm tra thông tin",
                                        Toast.LENGTH_LONG
                                    ).show()
                            })
                        }
                    },
                        shape = RoundedCornerShape(24)
                    ) {
                        Text(text = "Rút", fontSize = 20.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(horizontal = 10.dp))
                    }
                }
            }
        }
    }
}

