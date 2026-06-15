package com.example.wallet.data

data class WithdrawFundRequest(
    val user: Int,
    val amount: String,
    val upi_id: String
)
