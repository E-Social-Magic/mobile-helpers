package com.example.helpers.ui.screens.featureVideo

import androidx.compose.runtime.Immutable
import com.example.helpers.models.domain.model.Message

@Immutable
data class VideoItem(
    val id: String,
    val title:String,
    val content: String,
    val mediaUrl: String,
    val thumbnail: String,
    val lastPlayedPosition: Long = 0,
    val comments:List<Message>,
    val userName:String,
    val createdAt: String,
    val updatedAt: String,
    var votes: Int,
    val authorAvatar:String,
    val userId:String,
    )
