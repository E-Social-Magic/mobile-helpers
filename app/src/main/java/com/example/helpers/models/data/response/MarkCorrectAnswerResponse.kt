package com.example.helpers.models.data.response

import com.google.gson.annotations.SerializedName

data class MarkCorrectAnswerResponse(
    @SerializedName("user_id")
    val userId: String,
    val message: String
)
