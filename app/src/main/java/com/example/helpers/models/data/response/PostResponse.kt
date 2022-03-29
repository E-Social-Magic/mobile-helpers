package com.example.helpers.models.data.response

import com.google.gson.annotations.SerializedName

data class PostResponse(
@SerializedName("author_avatar")
val authorAvatar: String,
val blocked: Boolean,
val coins: Int,
val comments: List<Comment>,
val content: String,
val costs: Boolean,
val createdAt: String,
val expired: Long,
val hideName: Boolean,
val id: String,
val images: List<String>,
val private: Boolean,
val title: String,
val updatedAt: String,
@SerializedName("user_id")
val userId: String,
@SerializedName("username")
val userName: String,
val videos: List<String>,
val votedowns: List<String>,
val votes: Int,
val voteups: List<String>
)
