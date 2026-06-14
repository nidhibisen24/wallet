package com.example.wallet.data

data class ChatMessage(
    val id: Int,
    val sender_name: String,
    val sender_role: String,
    val message: String,
    val created_at: String
)
