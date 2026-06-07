package com.example.wallet.network


import com.example.wallet.data.AdminDashboardResponse
import com.example.wallet.data.LoginRequest
import com.example.wallet.data.LoginResponse
import com.example.wallet.data.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("login/")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @GET("admin-dashboard/")
    suspend fun getAdminDashboard(): AdminDashboardResponse

    @GET("all-users/")
    suspend fun getAllUsers(): List<User>
}

