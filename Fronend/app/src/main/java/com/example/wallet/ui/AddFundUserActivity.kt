package com.example.wallet.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.wallet.R
import com.example.wallet.data.AddFundRequest
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class AddFundUserActivity : AppCompatActivity() {

    private lateinit var etAmount: EditText
    private lateinit var etUtr: EditText
    private lateinit var imgQrCode: ImageView
    private lateinit var btnSubmit: Button
    private lateinit var btnBack: CardView

    private var userId = 0
    private var adminId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_fund_user)

        userId = intent.getIntExtra("USER_ID", 0)
        adminId = intent.getIntExtra("ADMIN_ID", 0)

        etAmount = findViewById(R.id.etAmount)
        etUtr = findViewById(R.id.etUtr)
        imgQrCode = findViewById(R.id.imgQrCode)
        btnSubmit = findViewById(R.id.btnSubmitRequest)

        loadQrCode()

        btnSubmit.setOnClickListener {
            submitFundRequest()
        }
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {

            finish()


        }
    }

    private fun loadQrCode() {

        lifecycleScope.launch {

            try {

                val qr =
                    RetrofitClient.api.getQrCode(adminId)


                val imageUrl = "http://13.233.182.165${qr.image}"

                Glide.with(this@AddFundUserActivity)
                    .load(imageUrl)
                    .into(imgQrCode)

            } catch (e: Exception) {

                Toast.makeText(
                    this@AddFundUserActivity,
                    "Failed to load QR Code",
                    Toast.LENGTH_SHORT
                ).show()

                e.printStackTrace()
            }
        }
    }

    private fun submitFundRequest() {

        val amount =
            etAmount.text.toString().trim()

        val utr =
            etUtr.text.toString().trim()

        if (amount.isEmpty()) {

            Toast.makeText(
                this,
                "Enter Amount",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (utr.isEmpty()) {

            Toast.makeText(
                this,
                "Enter UTR Number",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        lifecycleScope.launch {

            try {

                val response =
                    RetrofitClient.api.addFundRequest(

                        AddFundRequest(
                            user = userId,
                            admin = adminId,
                            amount = amount,
                            utr_number = utr
                        )
                    )

                Toast.makeText(
                    this@AddFundUserActivity,
                    response.message,
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            } catch (e: Exception) {

                Toast.makeText(
                    this@AddFundUserActivity,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()

                android.util.Log.e(
                    "ADD_FUND",
                    e.toString()
                )

                e.printStackTrace()
            }
        }
    }

}