package com.example.wallet.data

data class SavedPaymentDetails(
    val id: Int,
    val user: Int,
    val account_name: String,
    val upi_id: String?,
    val qr_code: String?,
    val is_default: Boolean,
    val created_at: String
)
