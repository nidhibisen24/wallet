package com.example.wallet.data

data class LoginResponse(
    val id: Int,
    val full_name: String,
    val mobile_number: String,
    val role: String,
    val message: String
)