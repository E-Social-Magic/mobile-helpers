package com.example.helpers.models.data.request

data class WithdrawCoinsRequest(
    val amount: Long,
    val phone: String,
    val displayName: String
)