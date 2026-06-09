package com.example.wallet.data


data class PendingRequest(
    val id: Int,
    val user_name: String,
    val mobile_number: String,
    val amount: String,
    val request_type: String,
    val status: String,
    val created_at: String
)