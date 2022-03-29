package com.example.helpers.ui.screens.featureProfile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helpers.models.data.repo.user.UserRepository
import com.example.helpers.models.data.response.DataX
import com.example.helpers.models.data.response.UserInfo
import com.example.helpers.util.Resource
import com.example.helpers.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    var images = mutableStateOf<List<String>>(listOf())
    var videos = mutableStateOf<List<String>>(listOf())
    var posts = mutableStateOf(0)
    var helped = mutableStateOf(0)
    var payment = mutableStateOf<List<DataX>>(listOf())
    var user = mutableStateOf<UserInfo?>(
        UserInfo(
            userName = "",
            email = "",
            avatar = "",
            address = "",
            phone = "",
            description = "",
            subjects = listOf(),
            follower = listOf(),
            following = listOf(),
            coins = 0,
            level = "",
            id = "",
        )
    )

    init {
       viewModelScope.launch {
           getUserInfo()
       }
    }

    suspend fun findUserById(id: String) {
        isLoading.value = true
        val result = userRepository.getUserInfo(id)
        try {
            when (result) {
                is Resource.Success -> {
                    if (result.data != null)
                        images.value = result.data.images
                    videos.value = result.data!!.videos
                    posts.value = result.data.sizePosts
                    helped.value = result.data.sizeHelped
                    user.value = userResponse2(result.data.user)
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

    suspend fun getUserInfo() {
        isLoading.value = true
//        if(id != null){
//            val result = userRepository.getUserInfo(id)
//        } else {
        val result = userRepository.getUserInfo()
//        }
        try {
            when (result) {
                is Resource.Success -> {
                    if (result.data != null)
                        images.value = result.data.images
                    videos.value = result.data!!.videos
                    posts.value = result.data.sizePosts
                    helped.value = result.data.sizeHelped
                    user.value = userResponse2(result.data.user)
                    payment.value = result.data.payment.mapIndexed { _, entry ->
                        paymentResponse2(paymentResponse = entry)
                    }
                    sessionManager.saveAuthCoins(result.data.user.coins.toString())
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

    private fun userResponse2(userResponse: UserInfo): UserInfo {
        return UserInfo(
            userName = userResponse.userName,
            email = userResponse.email,
            avatar = userResponse.avatar,
            address = userResponse.address,
            phone = userResponse.phone,
            description = userResponse.description,
            subjects = userResponse.subjects,
            follower = userResponse.follower,
            following = userResponse.following,
            coins = userResponse.coins,
            level = userResponse.level,
            id = userResponse.id,
        )
    }

    companion object {
        fun paymentResponse2(paymentResponse: DataX): DataX {
            return DataX(
                amount = paymentResponse.amount,
                createdAt = paymentResponse.createdAt,
                displayName = paymentResponse.displayName,
                id = paymentResponse.id,
                message = paymentResponse.message,
                orderId = paymentResponse.orderId,
                phone = paymentResponse.phone,
                requestId = paymentResponse.requestId,
                resultCode = paymentResponse.resultCode,
                updatedAt = paymentResponse.updatedAt,
                user_id = paymentResponse.user_id,
                username = paymentResponse.username,
                type = paymentResponse.type,
                accountBalance = paymentResponse.accountBalance
            )
        }
    }
}
