package com.example.wallet.data

data class AdminQRCode(
    val id: Int,
    val admin: Int,
    val name: String?,
    val image: String?,
    val image_url: String?,
    val is_active: Boolean,
    val uploaded_at: String
)