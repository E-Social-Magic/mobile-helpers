package com.example.helpers.models.domain.model

data class PostListEntry(
    val post:List<PostEntry>,
    val totalPages:Int,
    val currentPage:Int,
)
