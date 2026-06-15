package com.example.wallet.network


import com.example.wallet.data.AddFundRequest
import com.example.wallet.data.AdminDashboardResponse
import com.example.wallet.data.ApiMessageResponse
import com.example.wallet.data.ApproveRequest
import com.example.wallet.data.ApprovedRequest
import com.example.wallet.data.ChatMessage
import com.example.wallet.data.ChatRoom
import com.example.wallet.data.CreateRoomRequest
import com.example.wallet.data.CreateRoomResponse
import com.example.wallet.data.LoginRequest
import com.example.wallet.data.LoginResponse
import com.example.wallet.data.MessageResponse
import com.example.wallet.data.PendingRequest
import com.example.wallet.data.QrCodeResponse
import com.example.wallet.data.QrUploadResponse
import com.example.wallet.data.RegisterRequest
import com.example.wallet.data.RegisterResponse
import com.example.wallet.data.SendMessageRequest
import com.example.wallet.data.Transaction
import com.example.wallet.data.TransactionHistory
import com.example.wallet.data.User
import com.example.wallet.data.UserDashboardResponse
import com.example.wallet.data.UserDetails
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.DELETE
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

    @GET("user-dashboard/{userId}")
    suspend fun getUserDashboard(
        @Path("userId") userId: Int
    ): UserDashboardResponse

    // user apis
    @GET("all-users/")
    suspend fun getAllUsers(): List<User>

    @DELETE("delete-user/{id}/")
    suspend fun deleteUser(
        @Path("id") userId: Int
    ): Response<Unit>

    @GET("user-details/{id}")
    suspend fun getUserDetails(
        @Path("id") userId: Int
    ): UserDetails

    //fund

    @POST("add-fund-request/")
    suspend fun addFundRequest(
        @Body request: AddFundRequest
    ): MessageResponse


    @Multipart
    @POST("withdraw-fund-request/")
    fun withdrawFundRequest(
        @Part("user") user: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part("upi_id") upiId: RequestBody,
        @Part qr_code: MultipartBody.Part
    ): Call<ApiMessageResponse>


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

    @GET("approved-requests/")
    suspend fun getApprovedRequests(): List<ApprovedRequest>

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

    @GET("qr-code/")
    suspend fun getQrCode(): QrCodeResponse






    // history
    @GET("all-transactions/")
    suspend fun getAllTransactions(): List<TransactionHistory>




    //chat System
    @POST("create-chat-room/")
    fun createChatRoom(
        @Body request: CreateRoomRequest
    ): Call<CreateRoomResponse>


    @GET("chat-room-messages/{roomId}/")
    fun getMessages(
        @Path("roomId") roomId: Int
    ): Call<List<ChatMessage>>

    @POST("send-message/")
    fun sendMessage(
        @Body request: SendMessageRequest
    ): Call<Void>

    @GET("chat-rooms/")
    fun getChatRooms(): Call<List<ChatRoom>>

}

