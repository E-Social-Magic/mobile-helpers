package com.example.helpers.models.data.request

import okhttp3.RequestBody
import retrofit2.http.Part

data class NewPostRequest (
    @Part("title")
    val title: RequestBody,
    @Part("content")
    val content: RequestBody,
    @Part("hideName")
    val hideName: RequestBody,
    @Part("costs")
    val costs: RequestBody,
    @Part("expired")
    val expired: RequestBody,
    @Part("coins")
    val coins: RequestBody,
    @Part("group_id")
    val groupId:RequestBody
)

