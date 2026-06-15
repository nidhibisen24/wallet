package com.example.wallet.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wallet.R
import com.example.wallet.adapter.MessageAdapter
import com.example.wallet.data.ChatMessage
import com.example.wallet.data.SendMessageRequest
import com.example.wallet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity() {

    private lateinit var rvMessages: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var btnBack: CardView

    private var roomId = 0
    private var userId = 0

    private val messageList = mutableListOf<ChatMessage>()

    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        swipeRefresh = findViewById(R.id.swipeRefresh)
        rvMessages = findViewById(R.id.rvMessages)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)

        roomId = intent.getIntExtra("ROOM_ID", 0)
        userId = intent.getIntExtra("USER_ID", 0)

        adapter = MessageAdapter(messageList)

        rvMessages.layoutManager = LinearLayoutManager(this)
        rvMessages.adapter = adapter

        swipeRefresh.setOnRefreshListener {
            loadMessages(true)
        }

        btnSend.setOnClickListener {
            sendMessage()
        }


        loadMessages(false)
        btnBack = findViewById(R.id.btnBack)   // bug
        btnBack.setOnClickListener {

            finish()


        }
    }

    private fun loadMessages(showRefresh: Boolean = false) {

        if (showRefresh) {
            swipeRefresh.isRefreshing = true
        }

        RetrofitClient.api
            .getMessages(roomId)
            .enqueue(object : Callback<List<ChatMessage>> {

                override fun onResponse(
                    call: Call<List<ChatMessage>>,
                    response: Response<List<ChatMessage>>
                ) {

                    swipeRefresh.isRefreshing = false

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
                            this@ChatActivity,
                            "Failed to load messages",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<ChatMessage>>,
                    t: Throwable
                ) {

                    swipeRefresh.isRefreshing = false

                    Toast.makeText(
                        this@ChatActivity,
                        t.localizedMessage ?: "Network Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun sendMessage() {

        val message = etMessage.text.toString().trim()

        if (message.isEmpty()) {
            return
        }

        val request = SendMessageRequest(
            room = roomId,
            sender = userId,
            message = message
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

                        loadMessages(false)

                    } else {

                        Toast.makeText(
                            this@ChatActivity,
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
                        this@ChatActivity,
                        t.localizedMessage ?: "Network Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}