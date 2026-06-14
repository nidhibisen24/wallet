package com.example.wallet.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wallet.R
import com.example.wallet.data.ChatMessage
import com.example.wallet.data.SendMessageRequest
import com.example.wallet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminChatActivity : AppCompatActivity() {

    private lateinit var listMessages: ListView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button

    private var roomId = 0
    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_admin_chat)

        roomId = intent.getIntExtra(
            "ROOM_ID",
            0
        )

        userId = intent.getIntExtra(
            "USER_ID",
            0
        )

        listMessages =
            findViewById(R.id.listMessages)

        etMessage =
            findViewById(R.id.etMessage)

        btnSend =
            findViewById(R.id.btnSend)

        loadMessages()

        btnSend.setOnClickListener {

            sendMessage()
        }
    }

    private fun loadMessages() {

        RetrofitClient.api
            .getMessages(roomId)
            .enqueue(object :
                Callback<List<ChatMessage>> {

                override fun onResponse(
                    call: Call<List<ChatMessage>>,
                    response: Response<List<ChatMessage>>
                ) {

                    if (response.isSuccessful) {

                        val messages =
                            response.body() ?: emptyList()

                        val displayMessages =
                            messages.map {

                                "${it.sender_name}\n${it.message}"
                            }

                        val adapter =
                            ArrayAdapter(
                                this@AdminChatActivity,
                                android.R.layout.simple_list_item_1,
                                displayMessages
                            )

                        listMessages.adapter =
                            adapter
                    }
                }

                override fun onFailure(
                    call: Call<List<ChatMessage>>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@AdminChatActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun sendMessage() {

        val text =
            etMessage.text.toString().trim()

        if (text.isEmpty()) {

            Toast.makeText(
                this,
                "Enter message",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val request =
            SendMessageRequest(
                room = roomId,
                sender = 1, // Admin User ID
                message = text
            )

        RetrofitClient.api
            .sendMessage(request)
            .enqueue(object : Callback<Void> {

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {

                    if (response.isSuccessful) {

                        etMessage.setText("")
                        loadMessages()

                    }
                }

                override fun onFailure(
                    call: Call<Void>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@AdminChatActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()

                }
            })
    }

    override fun onResume() {
        super.onResume()

        loadMessages()
    }
}