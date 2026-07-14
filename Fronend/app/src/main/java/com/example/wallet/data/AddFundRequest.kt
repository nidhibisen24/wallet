package com.example.wallet.data

data class AddFundRequest(
    val user: Int,
    val admin:Int,
    val amount: String,
    val utr_number: String
)
