package com.example.helpers.models.data.response


data class TopicList(
    val groups: List<Topic>,
    val totalPages: Int,
    val currentPage: Int,
    val message:String,
)