package com.example.wallet.data

data class TransactionHistory(
    val id: Int,
    val user_id: Int,
    val user_name: String,
    val amount: String,
    val request_type: String,
    val status: String,
    val created_at: String
)
