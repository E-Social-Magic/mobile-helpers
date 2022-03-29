package com.example.helpers.models.data.response

data class SignUpResponse(
    val errors: List<ErrorSignUp>,
    val username:String,
    val email:String,
    val password:String
)