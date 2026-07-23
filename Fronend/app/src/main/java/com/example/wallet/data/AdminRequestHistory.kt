package com.example.wallet.data

data class AdminRequestHistory(

    val id: Int,

    val user_name: String,

    val request_type: String,

    val amount: String,

    val status: String,

    val created_at: String

)
