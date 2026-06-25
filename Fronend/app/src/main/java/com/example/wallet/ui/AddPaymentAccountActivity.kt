package com.example.wallet.ui


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.wallet.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import com.example.wallet.data.ApiResponse
import com.example.wallet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPaymentAccountActivity : AppCompatActivity() {

    private lateinit var btnBack: CardView

    private lateinit var etAccountName:
            com.google.android.material.textfield.TextInputEditText

    private lateinit var etUpiId:
            com.google.android.material.textfield.TextInputEditText

    private lateinit var imgQrPreview: ImageView

    private lateinit var btnChooseQr:
            com.google.android.material.button.MaterialButton

    private lateinit var btnSaveAccount:
            com.google.android.material.button.MaterialButton

    private lateinit var cbDefault: CheckBox

    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == Activity.RESULT_OK) {

                selectedImageUri = result.data?.data

                imgQrPreview.setImageURI(
                    selectedImageUri
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_add_payment_account
        )

        btnBack =
            findViewById(R.id.btnBack)

        etAccountName =
            findViewById(R.id.etAccountName)

        etUpiId =
            findViewById(R.id.etUpiId)

        imgQrPreview =
            findViewById(R.id.imgQrPreview)

        btnChooseQr =
            findViewById(R.id.btnChooseQr)

        btnSaveAccount =
            findViewById(R.id.btnSaveAccount)

        cbDefault =
            findViewById(R.id.cbDefault)

        btnBack.setOnClickListener {

            finish()

        }

        btnChooseQr.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_PICK
            )

            intent.type = "image/*"

            imagePickerLauncher.launch(
                intent
            )
        }

        btnSaveAccount.setOnClickListener {

            savePaymentAccount()

        }
    }
    private fun savePaymentAccount() {

        val accountName =
            etAccountName.text.toString().trim()

        val upiId =
            etUpiId.text.toString().trim()

        if (accountName.isEmpty()) {

            etAccountName.error =
                "Enter Account Name"

            return
        }

        if (upiId.isEmpty() && selectedImageUri == null) {

            Toast.makeText(
                this,
                "Enter UPI ID or Select QR Image",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val sharedPref =
            getSharedPreferences(
                "wallet_app",
                MODE_PRIVATE
            )

        val userId =
            sharedPref.getInt(
                "user_id",
                0
            )

        val userBody =
            userId.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())

        val accountBody =
            accountName
                .toRequestBody("text/plain".toMediaTypeOrNull())

        val upiBody =
            upiId
                .toRequestBody("text/plain".toMediaTypeOrNull())

        val defaultBody =
            cbDefault.isChecked
                .toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())

        val qrPart =
            createMultipartImage()

        RetrofitClient.api
            .addPaymentAccount(
                userBody,
                accountBody,
                upiBody,
                defaultBody,
                qrPart
            )
            .enqueue(object : Callback<ApiResponse> {

                override fun onResponse(
                    call: Call<ApiResponse>,
                    response: Response<ApiResponse>
                ) {

                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@AddPaymentAccountActivity,
                            "Payment Account Saved",
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()

                    } else {

                        Toast.makeText(
                            this@AddPaymentAccountActivity,
                            "Failed to Save",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@AddPaymentAccountActivity,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
    private fun getFileFromUri(
        uri: Uri
    ): File {

        val fileName =
            getFileName(uri)

        val file =
            File(cacheDir, fileName)

        contentResolver.openInputStream(uri)?.use {

            FileOutputStream(file).use { output ->

                it.copyTo(output)

            }
        }

        return file
    }

    private fun getFileName(
        uri: Uri
    ): String {

        var name = "image.jpg"

        val cursor =
            contentResolver.query(
                uri,
                null,
                null,
                null,
                null
            )

        cursor?.use {

            if (it.moveToFirst()) {

                val index =
                    it.getColumnIndex(
                        OpenableColumns.DISPLAY_NAME
                    )

                if (index >= 0) {

                    name =
                        it.getString(index)

                }
            }
        }

        return name
    }

    private fun createMultipartImage():
            MultipartBody.Part? {

        selectedImageUri ?: return null

        val file =
            getFileFromUri(
                selectedImageUri!!
            )

        val requestBody =
            file.asRequestBody(
                "image/*".toMediaTypeOrNull()
            )

        return MultipartBody.Part.createFormData(
            "qr_code",
            file.name,
            requestBody
        )
    }
}