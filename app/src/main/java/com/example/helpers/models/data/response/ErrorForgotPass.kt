package com.example.helpers.models.data.response

data class ErrorForgotPass(
    val location: String,
    val msg: String,
    val `param`: String,
    val value: String
)