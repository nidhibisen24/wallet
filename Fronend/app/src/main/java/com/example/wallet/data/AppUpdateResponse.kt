package com.example.wallet.data

data class AppUpdateResponse(
    val update_available: Boolean,
    val version_code: Int,
    val version_name: String,
    val force_update: Boolean,
    val apk_url: String
)
