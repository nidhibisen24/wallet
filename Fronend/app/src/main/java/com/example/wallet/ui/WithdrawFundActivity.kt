package com.example.wallet.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.wallet.R
import com.example.wallet.data.ApiMessageResponse
import com.example.wallet.network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class WithdrawFundActivity : AppCompatActivity() {


    private lateinit var etAmount: EditText
    private lateinit var etUpiId: EditText
    private lateinit var btnSelectQr: Button
    private lateinit var btnSubmit: Button
    private lateinit var ivQrPreview: ImageView
    private lateinit var btnBack: CardView

    private var selectedImageUri: Uri? = null
    private var userId = 0

    companion object {
        private const val PICK_IMAGE_REQUEST = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_withdraw_fund)

        userId = intent.getIntExtra(
            "USER_ID",
            0
        )

        etAmount = findViewById(R.id.etAmount)
        etUpiId = findViewById(R.id.etUpiId)
        btnSelectQr = findViewById(R.id.btnSelectQr)
        btnSubmit = findViewById(R.id.btnSubmit)
        ivQrPreview = findViewById(R.id.ivQrPreview)

        btnSelectQr.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

            startActivityForResult(
                intent,
                PICK_IMAGE_REQUEST
            )
        }

        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {

            finish()
        }

        btnSubmit.setOnClickListener {

            val amount =
                etAmount.text.toString().trim()

            val upiId =
                etUpiId.text.toString().trim()

            if (userId == 0) {

                Toast.makeText(
                    this,
                    "Invalid User",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (amount.isEmpty()) {

                etAmount.error = "Enter Amount"

                return@setOnClickListener
            }

            if (upiId.isEmpty()) {

                etUpiId.error = "Enter UPI ID"

                return@setOnClickListener
            }

            if (selectedImageUri == null) {

                Toast.makeText(
                    this,
                    "Select QR Code Image",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            uploadWithdrawRequest(
                amount,
                upiId
            )
        }
    }

    private fun uploadWithdrawRequest(
        amount: String,
        upiId: String
    ) {

        val imageFile =
            getFileFromUri(selectedImageUri!!)

        val requestFile =
            imageFile.asRequestBody(
                "image/*".toMediaTypeOrNull()
            )

        val qrPart =
            MultipartBody.Part.createFormData(
                "qr_code",
                imageFile.name,
                requestFile
            )

        val userBody =
            userId.toString()
                .toRequestBody(
                    "text/plain".toMediaTypeOrNull()
                )

        val amountBody =
            amount.toRequestBody(
                "text/plain".toMediaTypeOrNull()
            )

        val upiBody =
            upiId.toRequestBody(
                "text/plain".toMediaTypeOrNull()
            )

        btnSubmit.isEnabled = false

        RetrofitClient.api
            .withdrawFundRequest(
                userBody,
                amountBody,
                upiBody,
                qrPart
            )
            .enqueue(object :
                Callback<ApiMessageResponse> {

                override fun onResponse(
                    call: Call<ApiMessageResponse>,
                    response: Response<ApiMessageResponse>
                ) {

                    btnSubmit.isEnabled = true

                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@WithdrawFundActivity,
                            response.body()?.message
                                ?: "Withdraw fund request submitted",
                            Toast.LENGTH_LONG
                        ).show()

                        finish()

                    } else {

                        Toast.makeText(
                            this@WithdrawFundActivity,
                            "Failed : ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<ApiMessageResponse>,
                    t: Throwable
                ) {

                    btnSubmit.isEnabled = true

                    Toast.makeText(
                        this@WithdrawFundActivity,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun getFileFromUri(
        uri: Uri
    ): File {

        val inputStream =
            contentResolver.openInputStream(uri)

        val file = File(
            cacheDir,
            "withdraw_qr.jpg"
        )

        val outputStream =
            FileOutputStream(file)

        inputStream?.copyTo(outputStream)

        inputStream?.close()
        outputStream.close()

        return file
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (
            requestCode == PICK_IMAGE_REQUEST &&
            resultCode == Activity.RESULT_OK &&
            data != null &&
            data.data != null
        ) {

            selectedImageUri = data.data

            ivQrPreview.setImageURI(
                selectedImageUri
            )
        }
    }


}
