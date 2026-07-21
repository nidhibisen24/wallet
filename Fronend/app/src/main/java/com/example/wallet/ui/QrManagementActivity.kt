
package com.example.wallet.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.wallet.R
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileOutputStream
import java.io.InputStream

class QrManagementActivity : AppCompatActivity() {

    private lateinit var ivQrPreview: ImageView
    private lateinit var btnSelectQr: Button
    private lateinit var btnUploadQr: Button
    private lateinit var btnBack: CardView

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
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {

            finish()


        }
    }

    private fun uploadQr() {

        val uri = imageUri ?: return

        val sharedPref =
            getSharedPreferences(
                "wallet_app",
                MODE_PRIVATE
            )

        val adminId =
            sharedPref.getInt(
                "user_id",
                0
            )

        lifecycleScope.launch {

            try {

                val file =
                    getCompressedImage(uri)

                val requestFile =
                    file.asRequestBody(
                        "image/jpeg".toMediaTypeOrNull()
                    )

                val adminBody =
                    adminId.toString()
                        .toRequestBody(
                            "text/plain".toMediaTypeOrNull()
                        )

                val imagePart =
                    MultipartBody.Part.createFormData(
                        "image",
                        file.name,
                        requestFile
                    )

                val response =
                    RetrofitClient.api.uploadQrCode(
                        adminBody,
                        imagePart
                    )

                if (response.isSuccessful) {

                    android.widget.Toast.makeText(
                        this@QrManagementActivity,
                        "QR Uploaded Successfully",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()

                } else {

                    android.widget.Toast.makeText(
                        this@QrManagementActivity,
                        "Upload Failed",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {

                e.printStackTrace()

                android.widget.Toast.makeText(
                    this@QrManagementActivity,
                    e.localizedMessage,
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun getCompressedImage(
        uri: Uri
    ): File {

        val inputStream: InputStream =
            contentResolver.openInputStream(uri)!!

        val bitmap =
            BitmapFactory.decodeStream(inputStream)

        inputStream.close()

        val maxSize = 1024

        val ratio =
            bitmap.width.toFloat() / bitmap.height.toFloat()

        val width: Int
        val height: Int

        if (ratio > 1) {

            width = maxSize
            height = (maxSize / ratio).toInt()

        } else {

            height = maxSize
            width = (maxSize * ratio).toInt()
        }

        val resizedBitmap =
            Bitmap.createScaledBitmap(
                bitmap,
                width,
                height,
                true
            )

        val file =
            File(cacheDir, "compressed_qr.jpg")

        FileOutputStream(file).use {

            resizedBitmap.compress(
                Bitmap.CompressFormat.JPEG,
                75,
                it
            )
        }

        bitmap.recycle()
        resizedBitmap.recycle()

        return file
    }
}