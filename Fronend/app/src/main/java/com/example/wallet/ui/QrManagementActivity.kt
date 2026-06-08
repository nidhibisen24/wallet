
package com.example.wallet.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wallet.R
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class QrManagementActivity : AppCompatActivity() {

    private lateinit var ivQrPreview: ImageView
    private lateinit var btnSelectQr: Button
    private lateinit var btnUploadQr: Button

    private var imageUri: Uri? = null

    private val picker =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            imageUri = uri

            ivQrPreview.setImageURI(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_qr_management)

        ivQrPreview = findViewById(R.id.ivQrPreview)
        btnSelectQr = findViewById(R.id.btnSelectQr)
        btnUploadQr = findViewById(R.id.btnUploadQr)

        btnSelectQr.setOnClickListener {
            picker.launch("image/*")
        }

        btnUploadQr.setOnClickListener {
            uploadQr()
        }
    }

    private fun uploadQr() {

        val uri = imageUri ?: return

        lifecycleScope.launch {

            try {

                val inputStream =
                    contentResolver.openInputStream(uri)

                val file =
                    File(cacheDir, "qr_image.jpg")

                file.outputStream().use {
                    inputStream?.copyTo(it)
                }

                val requestFile =
                    file.asRequestBody(
                        "image/*".toMediaTypeOrNull()
                    )

                val imagePart =
                    MultipartBody.Part.createFormData(
                        "image",
                        file.name,
                        requestFile
                    )

                val response =
                    RetrofitClient.api.uploadQrCode(
                        imagePart
                    )

                if (response.isSuccessful) {

                    android.widget.Toast.makeText(
                        this@QrManagementActivity,
                        "QR Uploaded Successfully",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
}