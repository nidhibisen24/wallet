package com.example.wallet.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ChatActivity : AppCompatActivity() {

    private lateinit var rvMessages: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var btnImage: ImageButton
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var btnBack: CardView

    private var roomId = 0
    private var userId = 0

    private val messageList = mutableListOf<ChatMessage>()

    private lateinit var adapter: MessageAdapter

    // Gallery picker
    private val imagePicker =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {
                uploadImage(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        swipeRefresh = findViewById(R.id.swipeRefresh)
        rvMessages = findViewById(R.id.rvMessages)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        btnImage = findViewById(R.id.btnImage)
        btnBack = findViewById(R.id.btnBack)

        roomId = intent.getIntExtra("ROOM_ID", 0)
        userId = intent.getIntExtra("USER_ID", 0)

        adapter = MessageAdapter(messageList)

        rvMessages.layoutManager = LinearLayoutManager(this)
        rvMessages.adapter = adapter

        swipeRefresh.setOnRefreshListener {
            loadMessages(true)
        }

        // Send text message
        btnSend.setOnClickListener {
            sendMessage()
        }

        // Open gallery
        btnImage.setOnClickListener {
            imagePicker.launch("image/*")
        }

        // Back button
        btnBack.setOnClickListener {
            finish()
        }

        loadMessages(false)
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

    // ---------------------------------------------------------
    // SEND TEXT MESSAGE
    // ---------------------------------------------------------

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

    // ---------------------------------------------------------
    // UPLOAD IMAGE
    // ---------------------------------------------------------

    private fun uploadImage(uri: Uri) {

        btnImage.isEnabled = false

        try {

            val inputStream = contentResolver.openInputStream(uri)

            if (inputStream == null) {

                btnImage.isEnabled = true

                Toast.makeText(
                    this,
                    "Unable to open image",
                    Toast.LENGTH_SHORT
                ).show()

                return
            }

            // Copy selected image into cache
            val file = File(
                cacheDir,
                "chat_image_${System.currentTimeMillis()}.jpg"
            )

            inputStream.use { input ->

                file.outputStream().use { output ->

                    input.copyTo(output)
                }
            }

            // Create multipart file
            val requestFile = file
                .readBytes()
                .toRequestBody(
                    "image/*".toMediaTypeOrNull()
                )

            val imagePart =
                MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestFile
                )

            // room
            val roomBody = roomId
                .toString()
                .toRequestBody(
                    "text/plain".toMediaTypeOrNull()
                )

            // sender
            val senderBody = userId
                .toString()
                .toRequestBody(
                    "text/plain".toMediaTypeOrNull()
                )

            // message_type
            val messageTypeBody = "image"
                .toRequestBody(
                    "text/plain".toMediaTypeOrNull()
                )

            // Optional text with image
            val messageBody = etMessage
                .text
                .toString()
                .trim()
                .toRequestBody(
                    "text/plain".toMediaTypeOrNull()
                )

            RetrofitClient.api
                .sendImageMessage(
                    room = roomBody,
                    sender = senderBody,
                    messageType = messageTypeBody,
                    message = messageBody,
                    image = imagePart
                )
                .enqueue(object : Callback<ChatMessage> {

                    override fun onResponse(
                        call: Call<ChatMessage>,
                        response: Response<ChatMessage>
                    ) {

                        btnImage.isEnabled = true

                        if (response.isSuccessful) {

                            // Clear text if user typed a caption
                            etMessage.setText("")

                            // Reload chat
                            loadMessages(false)

                        } else {

                            Toast.makeText(
                                this@ChatActivity,
                                "Failed to send image",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        file.delete()
                    }

                    override fun onFailure(
                        call: Call<ChatMessage>,
                        t: Throwable
                    ) {

                        btnImage.isEnabled = true

                        Toast.makeText(
                            this@ChatActivity,
                            t.localizedMessage
                                ?: "Image upload failed",
                            Toast.LENGTH_SHORT
                        ).show()

                        file.delete()
                    }
                })

        } catch (e: Exception) {

            btnImage.isEnabled = true

            Toast.makeText(
                this,
                e.localizedMessage
                    ?: "Unable to upload image",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}