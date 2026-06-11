package com.example.wallet.data

data class UserDashboardResponse(
    val id: Int,
    val full_name: String,
    val mobile_number: String?,
    val wallet_balance: String?,
    val total_requests: Int,
    val pending_requests: Int
)