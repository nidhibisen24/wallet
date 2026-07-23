package com.example.wallet.data

data class UpdateProfileRequest(
    val user_id: Int,
    val full_name: String,
    val email: String
)
