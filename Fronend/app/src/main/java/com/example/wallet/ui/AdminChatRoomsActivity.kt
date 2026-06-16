package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wallet.R
import com.example.wallet.adapter.ChatRoomAdapter
import com.example.wallet.data.ChatRoom
import com.example.wallet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminChatRoomsActivity : AppCompatActivity() {

    private lateinit var rvRooms: RecyclerView
    private lateinit var btnBack: CardView

    private lateinit var swipeRefresh: SwipeRefreshLayout

    private var rooms: List<ChatRoom> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_admin_chat_rooms
        )

        rvRooms =
            findViewById(R.id.rvRooms)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        rvRooms.layoutManager =
            LinearLayoutManager(this)

        loadRooms()

        swipeRefresh.setOnRefreshListener {
            loadRooms()
        }
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {

            finish()


        }
    }

    private fun loadRooms() {
        swipeRefresh.isRefreshing = true

        RetrofitClient.api.getChatRooms()
            .enqueue(object :
                Callback<List<ChatRoom>> {

                override fun onResponse(
                    call: Call<List<ChatRoom>>,
                    response: Response<List<ChatRoom>>
                ) {
                    swipeRefresh.isRefreshing = false

                    if (response.isSuccessful) {

                        rooms =
                            response.body() ?: listOf()

                        rvRooms.adapter =
                            ChatRoomAdapter(
                                rooms
                            ) { room ->

                                val intent =
                                    Intent(
                                        this@AdminChatRoomsActivity,
                                        AdminChatActivity::class.java
                                    )

                                intent.putExtra(
                                    "ROOM_ID",
                                    room.room_id
                                )

                                intent.putExtra(
                                    "USER_ID",
                                    room.user_id
                                )

                                startActivity(intent)
                            }
                    }
                }

                override fun onFailure(
                    call: Call<List<ChatRoom>>,
                    t: Throwable
                ) {
                    swipeRefresh.isRefreshing = false

                    Toast.makeText(
                        this@AdminChatRoomsActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}