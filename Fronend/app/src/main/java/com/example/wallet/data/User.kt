package com.example.wallet.data

data class User(
    val id: Int,
    val full_name: String,
    val mobile_number: String,
    val role: String,
    val wallet_balance: String?
)
