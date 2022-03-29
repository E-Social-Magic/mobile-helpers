package com.example.helpers.models.data.response

data class UserResponse (
    var user: UserInfo,
    val sizePosts: Int,
    val sizeHelped: Int,
    val payment: List<DataX>,
    val images: List<String>,
    val videos: List<String>,
    var message: String
)