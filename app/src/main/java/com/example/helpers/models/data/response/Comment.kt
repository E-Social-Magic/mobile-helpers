package com.example.helpers.models.data.response

import com.google.gson.annotations.SerializedName

data class Comment(
    val _id: String,
    @SerializedName("username")
    val userName:String,
    val correct :Boolean,
    val comment: String,
    @SerializedName("user_id")
    val userId: String,
    val images:List<String>,
    val avatar:String?,
    val votes:Int,
    val voteups:List<String>,
    val votedowns:List<String>
)