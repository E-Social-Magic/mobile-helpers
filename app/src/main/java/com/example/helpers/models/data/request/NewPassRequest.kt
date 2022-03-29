package com.example.helpers.models.data.request

data class NewPassRequest(
    var email: String,
    var code: String,
    var password: String,
    var confirm: String
)
