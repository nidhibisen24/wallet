package com.example.wallet.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

//    private const val BASE_URL = "https://8924-152-59-31-44.ngrok-free.app/api/"
//    private const val BASE_URL = "http://13.60.58.242:8000/api/"
    private const val BASE_URL = "http://10.165.159.70:5000/api/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
