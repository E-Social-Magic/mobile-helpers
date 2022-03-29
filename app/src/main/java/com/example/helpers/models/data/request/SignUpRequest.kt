package com.example.helpers.models.data.request

data class SignUpRequest(
    val confirm: String,
    val email: String,
    val phone :String,
    val password: String,
    val username: String
)