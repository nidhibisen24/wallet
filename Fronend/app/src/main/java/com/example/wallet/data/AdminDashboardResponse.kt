package com.example.wallet.data

data class AdminDashboardResponse(
    val total_users: Int,
    val pending_requests: Int,
    val approved_requests: Int,
    val rejected_requests: Int
)