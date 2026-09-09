package com.example.wallet.data

data class ChatMessage(
    val id: Int,
    val sender: Int,
    val sender_name: String,
    val sender_role: String,
    val message_type: String,
    val message: String?,
    val image: String?,
    val is_read: Boolean,
    val created_at: String
)
