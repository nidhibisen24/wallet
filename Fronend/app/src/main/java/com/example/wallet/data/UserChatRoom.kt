package com.example.wallet.data

data class UserChatRoom(
    val room_id: Int,
    val user_id: Int,
    val admin_id: Int,
    val full_name: String,
    val mobile_number: String,
    val updated_at: String
)
