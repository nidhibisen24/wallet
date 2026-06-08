package com.example.wallet.network


import com.example.wallet.data.AdminDashboardResponse
import com.example.wallet.data.ApproveRequest
import com.example.wallet.data.LoginRequest
import com.example.wallet.data.LoginResponse
import com.example.wallet.data.PendingRequest
import com.example.wallet.data.QrUploadResponse
import com.example.wallet.data.RegisterRequest
import com.example.wallet.data.RegisterResponse
import com.example.wallet.data.Transaction
import com.example.wallet.data.User
import com.example.wallet.data.UserDetails
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("login/")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @POST("register/")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): RegisterResponse

    @GET("admin-dashboard/")
    suspend fun getAdminDashboard(): AdminDashboardResponse


    // user apis
    @GET("all-users/")
    suspend fun getAllUsers(): List<User>

    @GET("user-details/{id}")
    suspend fun getUserDetails(
        @Path("id") userId: Int
    ): UserDetails

    @GET("my-request/{id}")
    suspend fun getUserHistory(
        @Path("id") userId: Int
    ): List<Transaction>

    // request apis
    @GET("pending-request/")
    suspend fun getPendingRequests(): List<PendingRequest>

    @POST("approve-request/")
    suspend fun approveRequest(
        @Body request: ApproveRequest
    ): Response<Unit>

    @POST("reject-request/")
    suspend fun rejectRequest(
        @Body request: ApproveRequest
    ): Response<Unit>


    // qr code
    @Multipart
    @POST("upload-qr-code/")
    suspend fun uploadQrCode(
        @Part image: MultipartBody.Part
    ): Response<QrUploadResponse>


}

