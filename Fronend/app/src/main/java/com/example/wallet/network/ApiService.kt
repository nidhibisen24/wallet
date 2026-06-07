package com.example.wallet.network


import com.example.wallet.data.LoginRequest
import com.example.wallet.data.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login/")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>
}

