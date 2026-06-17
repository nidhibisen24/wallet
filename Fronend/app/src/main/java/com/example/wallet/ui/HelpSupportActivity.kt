package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.wallet.R
import com.example.wallet.data.CreateRoomRequest
import com.example.wallet.data.CreateRoomResponse
import com.example.wallet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HelpSupportActivity : AppCompatActivity() {

    private lateinit var btnStartChat: Button
    private lateinit var btnBack: CardView

    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_help_support)

        userId =
            intent.getIntExtra(
                "USER_ID",
                0
            )

        btnStartChat =
            findViewById(R.id.btnStartChat)

        btnStartChat.setOnClickListener {

            createChatRoom()
        }
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {

            finish()


        }
    }

    private fun createChatRoom() {

        RetrofitClient.api.createChatRoom(
            CreateRoomRequest(userId)
        ).enqueue(
            object : Callback<CreateRoomResponse> {

                override fun onResponse(
                    call: Call<CreateRoomResponse>,
                    response: Response<CreateRoomResponse>
                ) {

                    if (response.isSuccessful &&
                        response.body() != null
                    ) {

                        val roomId =
                            response.body()!!.room_id

                        val intent =
                            Intent(
                                this@HelpSupportActivity,
                                ChatActivity::class.java
                            )

                        intent.putExtra(
                            "ROOM_ID",
                            roomId
                        )

                        intent.putExtra(
                            "USER_ID",
                            userId
                        )

                        startActivity(intent)

                    } else {

                        Toast.makeText(
                            this@HelpSupportActivity,
                            "Failed to create room",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<CreateRoomResponse>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@HelpSupportActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }
}