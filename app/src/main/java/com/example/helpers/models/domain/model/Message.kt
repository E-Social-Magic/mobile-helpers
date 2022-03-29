package com.example.helpers.models.domain.model

import com.example.helpers.models.Constants


data class Message(
    var authorName: String?=Constants.Anonymous,
    var avatarAuthor: String?=null,
    var message: String,
    var images: List<String>?=null,
    val isCorrect :Boolean,
    val userId:String,
    val id :String
)
