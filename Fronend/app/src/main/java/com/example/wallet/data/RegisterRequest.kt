package com.example.wallet.data

data class RegisterRequest(
    val full_name: String,
    val mobile_number: String,
    val email: String,
    val password: String,
    val referral_code: String
)
