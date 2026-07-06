package com.example.wallet.data



data class CreateAdminRequest(

    val super_admin_id: Int,

    val full_name: String,

    val mobile_number: String,

    val password: String
)