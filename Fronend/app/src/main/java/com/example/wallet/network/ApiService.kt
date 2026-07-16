package com.example.wallet.network


import com.example.wallet.data.AddBonusRequest
import com.example.wallet.data.AddBonusResponse
import com.example.wallet.data.AddFundRequest
import com.example.wallet.data.AddPaymentAccountRequest
import com.example.wallet.data.Admin
import com.example.wallet.data.AdminDashboardResponse
import com.example.wallet.data.ApiMessageResponse
import com.example.wallet.data.ApiResponse
import com.example.wallet.data.ApproveRequest
import com.example.wallet.data.ApprovedRequest
import com.example.wallet.data.ChatMessage
import com.example.wallet.data.ChatRoom
import com.example.wallet.data.CreateAdminRequest
import com.example.wallet.data.CreateAdminResponse
import com.example.wallet.data.CreateRoomRequest
import com.example.wallet.data.CreateRoomResponse
import com.example.wallet.data.LoginRequest
import com.example.wallet.data.LoginResponse
import com.example.wallet.data.MessageResponse
import com.example.wallet.data.MyReferralResponse
import com.example.wallet.data.PendingRequest
import com.example.wallet.data.QrCodeResponse
import com.example.wallet.data.QrUploadResponse
import com.example.wallet.data.RegisterRequest
import com.example.wallet.data.RegisterResponse
import com.example.wallet.data.SavedPaymentDetails
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
import retrofit2.http.Query

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

    @POST("add-bonus/")
    suspend fun addBonus(
        @Body request: AddBonusRequest
    ): AddBonusResponse

    @Multipart
    @POST("withdraw-fund-request/")
    fun withdrawFundRequest(

        @Part("user")
        user: RequestBody,

        @Part("admin")
        admin: RequestBody,

        @Part("amount")
        amount: RequestBody,

        @Part("payment_account")
        paymentAccount: RequestBody

    ): Call<ApiMessageResponse>


    @GET("my-request/{id}")
    suspend fun getUserHistory(
        @Path("id") userId: Int
    ): List<Transaction>

    // request apis
    @GET("pending-request/{adminId}/")
    suspend fun getPendingRequests(
        @Path("adminId") adminId: Int
    ): List<PendingRequest>

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
        @Part("admin") admin: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<QrUploadResponse>

    @GET("qr-code/")
    suspend fun getQrCode(
        @Query("admin_id") adminId: Int
    ): QrCodeResponse






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
    fun getChatRooms(
        @Query("admin_id") adminId: Int
    ): Call<List<ChatRoom>>


    @Multipart
    @POST("add-payment-account/")
    fun addPaymentAccount(

        @Part("user") user: RequestBody,

        @Part("account_name") accountName: RequestBody,

        @Part("upi_id") upiId: RequestBody,

        @Part("is_default") isDefault: RequestBody,

        @Part qr_code: MultipartBody.Part?

    ): Call<ApiResponse>


    @GET("payment-accounts/{userId}/")
    fun getPaymentAccounts(
        @Path("userId") userId: Int
    ): Call<List<SavedPaymentDetails>>



    @GET("my-referral/{userId}/")
    suspend fun getMyReferral(
        @Path("userId") userId: Int
    ): MyReferralResponse


    @POST("create-admin/")
    fun createAdmin(

        @Body request: CreateAdminRequest

    ): Call<CreateAdminResponse>

    @GET("admins/")
    fun getAllAdmins(): Call<List<Admin>>
}

