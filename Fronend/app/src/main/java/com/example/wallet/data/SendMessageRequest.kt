package com.example.wallet.data

data class SendMessageRequest(
    val room: Int,
    val sender: Int,
    val message: String
)