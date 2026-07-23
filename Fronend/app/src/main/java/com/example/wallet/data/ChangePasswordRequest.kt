package com.example.wallet.data

data class ChangePasswordRequest(
    val user_id: Int,
    val current_password: String,
    val new_password: String,
    val confirm_password: String
)
