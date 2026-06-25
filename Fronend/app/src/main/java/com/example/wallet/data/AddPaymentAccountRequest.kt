package com.example.wallet.data


data class AddPaymentAccountRequest(

    val user: Int,

    val account_name: String,

    val upi_id: String,

    val is_default: Boolean
)
