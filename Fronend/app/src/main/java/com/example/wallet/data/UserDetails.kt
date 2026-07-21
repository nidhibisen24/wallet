package com.example.wallet.data

data class UserDetails(
    val id: Int,
    val full_name: String,
    val mobile_number: String,
    val role: String,
    val wallet_balance: String,
    val total_requests: Int,
    val pending_requests: Int,
    val is_blocked: Boolean
)
