package com.example.wallet.data

data class AdminDetailsResponse(

    val id: Int,
    val full_name: String,
    val mobile_number: String,
    val email: String,
    val role: String,
    val wallet_balance: String,

    val is_blocked: Boolean,
    val total_requests: Int,
    val pending_requests: Int,
    val history: List<AdminRequestHistory>

)
