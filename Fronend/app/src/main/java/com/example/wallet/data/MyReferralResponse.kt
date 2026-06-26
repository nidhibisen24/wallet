package com.example.wallet.data

data class MyReferralResponse(

    val my_referral_code: String,

    val total_referrals: Int,

    val total_bonus: Double,

    val referrals: List<ReferralHistory>
)
