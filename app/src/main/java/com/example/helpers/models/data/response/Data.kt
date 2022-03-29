package com.example.helpers.models.data.response

data class Data(
    val amount: Int,
    val message: String,
    val orderId: String,
    val partnerCode: String,
    val payUrl: String,
    val requestId: String,
    val responseTime: Long,
    val resultCode: Int
)