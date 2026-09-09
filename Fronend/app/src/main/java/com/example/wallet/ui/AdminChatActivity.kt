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
import com.example.wallet.R
import com.example.wallet.adapter.AdminChatAdapter
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

class AdminChatActivity : AppCompatActivity() {

    private lateinit var rvMessages: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var btnImage: ImageButton
    private lateinit var btnBack: CardView

    private var roomId = 0
    private var adminId = 0

    private val messageList = mutableListOf<ChatMessage>()

    private lateinit var adapter: AdminChatAdapter

    // ---------------------------------------------------------
    // IMAGE PICKER
    // ---------------------------------------------------------

    private val imagePicker =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {
                uploadImage(uri)
            }
        }

    // ---------------------------------------------------------
    // ON CREATE
    // ---------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(
            android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )

        setContentView(R.layout.activity_admin_chat)

        roomId = intent.getIntExtra("ROOM_ID", 0)

        val sharedPref =
            getSharedPreferences(
                "wallet_app",
                MODE_PRIVATE
            )

        adminId = sharedPref.getInt(
            "user_id",
            0
        )

        rvMessages = findViewById(R.id.rvMessages)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        btnImage = findViewById(R.id.btnImage)
        btnBack = findViewById(R.id.btnBack)

        adapter = AdminChatAdapter(messageList)

        rvMessages.layoutManager =
            LinearLayoutManager(this)

        rvMessages.adapter = adapter

        // ---------------------------------------------------------
        // SEND TEXT
        // ---------------------------------------------------------

        btnSend.setOnClickListener {
            sendMessage()
        }

        // ---------------------------------------------------------
        // OPEN GALLERY
        // ---------------------------------------------------------

        btnImage.setOnClickListener {
            imagePicker.launch("image/*")
        }

        // ---------------------------------------------------------
        // BACK
        // ---------------------------------------------------------

        btnBack.setOnClickListener {
            finish()
        }

        loadMessages()
    }

    // ---------------------------------------------------------
    // LOAD MESSAGES
    // ---------------------------------------------------------

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

    // ---------------------------------------------------------
    // SEND TEXT MESSAGE
    // ---------------------------------------------------------

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
            sender = adminId,
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
                            response.code().toString(),
                            Toast.LENGTH_LONG
                        ).show()

                        println(
                            response.errorBody()?.string()
                        )
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

    // ---------------------------------------------------------
    // UPLOAD IMAGE MESSAGE
    // ---------------------------------------------------------

    private fun uploadImage(uri: Uri) {

        btnImage.isEnabled = false

        try {

            val inputStream =
                contentResolver.openInputStream(uri)

            if (inputStream == null) {

                btnImage.isEnabled = true

                Toast.makeText(
                    this,
                    "Unable to open image",
                    Toast.LENGTH_SHORT
                ).show()

                return
            }

            // Copy selected image to cache
            val file = File(
                cacheDir,
                "chat_image_${System.currentTimeMillis()}.jpg"
            )

            inputStream.use { input ->

                file.outputStream().use { output ->

                    input.copyTo(output)
                }
            }

            // -----------------------------------------------------
            // IMAGE REQUEST BODY
            // -----------------------------------------------------

            val requestFile =
                file.readBytes().toRequestBody(
                    "image/*".toMediaTypeOrNull()
                )

            // -----------------------------------------------------
            // MULTIPART IMAGE
            // -----------------------------------------------------

            val imagePart =
                MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestFile
                )

            // -----------------------------------------------------
            // ROOM
            // -----------------------------------------------------

            val roomBody =
                roomId.toString().toRequestBody(
                    "text/plain".toMediaTypeOrNull()
                )

            // -----------------------------------------------------
            // SENDER
            // -----------------------------------------------------

            val senderBody =
                adminId.toString().toRequestBody(
                    "text/plain".toMediaTypeOrNull()
                )

            // -----------------------------------------------------
            // MESSAGE TYPE
            // -----------------------------------------------------

            val messageTypeBody =
                "image".toRequestBody(
                    "text/plain".toMediaTypeOrNull()
                )

            // -----------------------------------------------------
            // OPTIONAL CAPTION
            // -----------------------------------------------------

            val messageBody =
                etMessage.text.toString()
                    .trim()
                    .toRequestBody(
                        "text/plain".toMediaTypeOrNull()
                    )

            // -----------------------------------------------------
            // SEND TO DJANGO
            // -----------------------------------------------------

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

                            etMessage.setText("")

                            loadMessages()

                        } else {

                            Toast.makeText(
                                this@AdminChatActivity,
                                "Failed to send image: ${response.code()}",
                                Toast.LENGTH_LONG
                            ).show()

                            println(
                                response.errorBody()?.string()
                            )
                        }

                        file.delete()
                    }

                    override fun onFailure(
                        call: Call<ChatMessage>,
                        t: Throwable
                    ) {

                        btnImage.isEnabled = true

                        Toast.makeText(
                            this@AdminChatActivity,
                            t.localizedMessage
                                ?: "Image upload failed",
                            Toast.LENGTH_LONG
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
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // ---------------------------------------------------------
    // RESUME
    // ---------------------------------------------------------

    override fun onResume() {
        super.onResume()
        loadMessages()
    }
}