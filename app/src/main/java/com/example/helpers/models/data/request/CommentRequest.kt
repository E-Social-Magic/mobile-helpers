package com.example.helpers.models.data.request

import okhttp3.RequestBody
import retrofit2.http.Part

data class CommentRequest(
    @Part("comment")
    val comment: RequestBody,
)
