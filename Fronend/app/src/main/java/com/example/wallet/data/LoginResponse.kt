package com.example.wallet.data

data class LoginResponse(
    val message: String,
    val user_id: Int,
    val full_name: String,
    val mobile_number: String,
    val role: String,
    val wallet_balance: String
)