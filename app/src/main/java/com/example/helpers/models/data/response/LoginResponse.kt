package com.example.helpers.models.data.response

data class LoginResponse(
    val email: String,
    val id: String,
    val role: String,
    val subjects: List<Topic>,
    val token: String,
    val updatedAt: String,
    val username: String,
    val message:String?,
    val avatar:String="https://gaplo.tech/content/images/2020/03/android-jetpack.jpg",
)