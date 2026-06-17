package com.example.wallet.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.adapter.AdminChatAdapter
import com.example.wallet.data.ChatMessage
import com.example.wallet.data.SendMessageRequest
import com.example.wallet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminChatActivity : AppCompatActivity() {

    private lateinit var rvMessages: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var btnBack: CardView

    private var roomId = 0
    private var userId = 0

    private val messageList = mutableListOf<ChatMessage>()

    private lateinit var adapter: AdminChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(
            android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
        setContentView(R.layout.activity_admin_chat)

        roomId = intent.getIntExtra("ROOM_ID", 0)

        userId = intent.getIntExtra("USER_ID", 0)

        rvMessages = findViewById(R.id.rvMessages)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        btnBack = findViewById(R.id.btnBack)

        adapter = AdminChatAdapter(messageList)

        rvMessages.layoutManager =
            LinearLayoutManager(this)

        rvMessages.adapter = adapter

        btnSend.setOnClickListener {
            sendMessage()
        }

        btnBack.setOnClickListener {
            finish()
        }

        loadMessages()
    }

    private fun loadMessages() {

        RetrofitClient.api
            .getMessages(roomId)
            .enqueue(object : Callback<List<ChatMessage>> {

                override fun onResponse(
                    call: Call<List<ChatMessage>>,
                    response: Response<List<ChatMessage>>
                ) {

                    if (response.isSuccessful) {

                        messageList.clear()

                        response.body()?.let {
                            messageList.addAll(it)
                        }

                        adapter.notifyDataSetChanged()

                        if (messageList.isNotEmpty()) {

                            rvMessages.scrollToPosition(
                                messageList.size - 1
                            )
                        }

                    } else {

                        Toast.makeText(
                            this@AdminChatActivity,
                            "Failed to load messages",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<ChatMessage>>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@AdminChatActivity,
                        t.localizedMessage ?: "Network Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun sendMessage() {

        val text = etMessage.text.toString().trim()

        if (text.isEmpty()) {

            Toast.makeText(
                this,
                "Enter message",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val request = SendMessageRequest(
            room = roomId,
            sender = 1,
            message = text
        )

        btnSend.isEnabled = false

        RetrofitClient.api
            .sendMessage(request)
            .enqueue(object : Callback<Void> {

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {

                    btnSend.isEnabled = true

                    if (response.isSuccessful) {

                        etMessage.setText("")

                        loadMessages()

                    } else {

                        Toast.makeText(
                            this@AdminChatActivity,
                            "Failed to send message",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<Void>,
                    t: Throwable
                ) {

                    btnSend.isEnabled = true

                    Toast.makeText(
                        this@AdminChatActivity,
                        t.localizedMessage ?: "Network Error",
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