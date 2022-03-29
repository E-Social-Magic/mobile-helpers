package com.example.helpers.models.domain.model


data class PostEntry(
    val id: String,
    val title: String,
    val content: String,
    val images: List<String>,
    val comments:List<Message>,
    var votes: Int,
    val videos: List<String>,
    val userId: String,
    val userName:String,
    val costs: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val authorAvatar:String,
    val hideName:Boolean,
    val expired:Long,
    val coins:Int
)