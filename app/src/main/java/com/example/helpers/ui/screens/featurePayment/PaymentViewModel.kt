package com.example.helpers.ui.screens.featurePayment

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helpers.models.data.repo.user.UserRepository
import com.example.helpers.models.data.request.DepositCoinsRequest
import com.example.helpers.models.data.request.WithdrawCoinsRequest
import com.example.helpers.models.data.response.DataX
import com.example.helpers.models.data.response.WithdrawCoinsResponse
import com.example.helpers.ui.screens.featureProfile.UserViewModel
import com.example.helpers.util.Resource
import com.example.helpers.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {

    val coins = sessionManager.fetchCoin()
    val assumeBalance = mutableStateOf(10000L)
    var payment = mutableStateOf<List<DataX>>(listOf())
    val isLoading = mutableStateOf(false)
    var avatar = mutableStateOf<String>("")
    var phone = mutableStateOf("0")
    var name = mutableStateOf("")
    val result = mutableStateOf<WithdrawCoinsResponse>(
        WithdrawCoinsResponse(
            data = DataX(
                amount = 0, createdAt = "", id = "",
                message = "",
                orderId = "",
                requestId = "",
                resultCode = "",
                updatedAt = "",
                user_id = "",
                username = "",
                phone = "",
                type = "",
                displayName = "",
                accountBalance = 0L
            ), message = ""
        )
    )

    fun onCoinChange(newCoins: Long) {
        assumeBalance.value = newCoins
    }

    fun onNameChange(newName: String) {
        name.value = newName
    }

    fun onPhoneChange(newPhone: String) {
        phone.value = newPhone
    }

    fun depositCoins(action: (String) -> Unit) {
        viewModelScope.launch {
            val momoResponse =
                userRepository.depositCoins(DepositCoinsRequest(amount = assumeBalance.value))
            if (momoResponse.data != null) {
                val payUrl = momoResponse.data.data.payUrl
                if (!payUrl.isEmpty()) {
                    action.invoke(payUrl)
                }
            }
        }
    }

    fun withDraw(action: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (assumeBalance.value > coins) {
                action.invoke(false)
                return@launch
            }
            val momoResponse =
                userRepository.withdrawCoins(
                    withdrawCoinsResponse = WithdrawCoinsRequest(
                        displayName = name.value,
                        phone = phone.value,
                        amount = assumeBalance.value
                    )
                )
            if (momoResponse.data != null) {
                if (momoResponse.data.data.resultCode == "7000") {
                    action.invoke(true)
                } else
                    action.invoke(false)
            }
        }
    }

    fun getHistoryPayment() {
        viewModelScope.launch {
            isLoading.value = true
            val result = userRepository.getUserInfo()
            try {
                when (result) {
                    is Resource.Success -> {
                        if (result.data != null)
                            payment.value = result.data.payment.mapIndexed { _, entry ->
                                UserViewModel.paymentResponse2(paymentResponse = entry)
                            }
                        sessionManager.saveAuthCoins(result.data!!.user.coins.toString())
                    }
                    is Resource.Error -> {
                    }
                    else -> {
                    }
                }
            } catch (e: Exception) {
            }
            isLoading.value = false
        }
    }
}
