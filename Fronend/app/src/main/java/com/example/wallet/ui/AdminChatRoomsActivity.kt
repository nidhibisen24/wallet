package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wallet.R
import com.example.wallet.data.ChatRoom
import com.example.wallet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminChatRoomsActivity : AppCompatActivity() {

    private lateinit var listRooms: ListView

    private var rooms: List<ChatRoom> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_admin_chat_rooms
        )

        listRooms =
            findViewById(R.id.listRooms)

        loadRooms()
    }

    private fun loadRooms() {

        RetrofitClient.api.getChatRooms()
            .enqueue(object :
                Callback<List<ChatRoom>> {

                override fun onResponse(
                    call: Call<List<ChatRoom>>,
                    response: Response<List<ChatRoom>>
                ) {

                    if (response.isSuccessful) {

                        rooms =
                            response.body() ?: listOf()

                        val names =
                            rooms.map { it.name }

                        val adapter =
                            ArrayAdapter(
                                this@AdminChatRoomsActivity,
                                android.R.layout.simple_list_item_1,
                                names
                            )

                        listRooms.adapter =
                            adapter

                        listRooms.setOnItemClickListener {
                                _, _, position, _ ->

                            val room =
                                rooms[position]

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

                    Toast.makeText(
                        this@AdminChatRoomsActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}