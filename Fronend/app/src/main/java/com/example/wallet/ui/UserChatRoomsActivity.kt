package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.adapter.UserChatRoomAdapter
import com.example.wallet.data.UserChatRoom
import com.example.wallet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserChatRoomsActivity : AppCompatActivity() {

    private lateinit var rvRooms: RecyclerView
    private lateinit var btnBack: CardView

    private val rooms = mutableListOf<UserChatRoom>()

    private lateinit var adapter: UserChatRoomAdapter

    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_user_chat_rooms
        )

        val sharedPref =
            getSharedPreferences(
                "wallet_app",
                MODE_PRIVATE
            )

        userId =
            sharedPref.getInt(
                "user_id",
                0
            )

        rvRooms =
            findViewById(R.id.rvRooms)

        btnBack =
            findViewById(R.id.btnBack)

        rvRooms.layoutManager =
            LinearLayoutManager(this)

        adapter = UserChatRoomAdapter(
            rooms
        ) { room ->

            val intent =
                Intent(
                    this@UserChatRoomsActivity,
                    ChatActivity::class.java
                )

            intent.putExtra(
                "ROOM_ID",
                room.room_id
            )

            intent.putExtra(
                "USER_ID",
                userId
            )

            startActivity(intent)
        }

        rvRooms.adapter = adapter

        btnBack.setOnClickListener {
            finish()
        }

        loadRooms()
    }

    private fun loadRooms() {

        RetrofitClient.api
            .getUserChatRooms(userId)
            .enqueue(
                object :
                    Callback<List<UserChatRoom>> {

                    override fun onResponse(
                        call: Call<List<UserChatRoom>>,
                        response: Response<List<UserChatRoom>>
                    ) {

                        if (response.isSuccessful) {

                            rooms.clear()

                            response.body()?.let {
                                rooms.addAll(it)
                            }

                            adapter.notifyDataSetChanged()

                        } else {

                            Toast.makeText(
                                this@UserChatRoomsActivity,
                                "Failed to load chat rooms",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<List<UserChatRoom>>,
                        t: Throwable
                    ) {

                        Toast.makeText(
                            this@UserChatRoomsActivity,
                            t.localizedMessage
                                ?: "Network Error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
    }

    override fun onResume() {
        super.onResume()

        if (userId != 0) {
            loadRooms()
        }
    }
}