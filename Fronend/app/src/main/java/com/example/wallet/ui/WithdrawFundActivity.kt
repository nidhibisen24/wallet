package com.example.wallet.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.example.wallet.R
import com.example.wallet.data.ApiMessageResponse
import com.example.wallet.data.SavedPaymentDetails
import com.example.wallet.network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull

import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WithdrawFundActivity : AppCompatActivity() {


    private lateinit var etAmount: EditText
    private lateinit var btnSubmit: Button

    private lateinit var tvSelectedUpi: TextView
    private lateinit var ivQrPreview: ImageView
    private lateinit var btnBack: CardView


    private var userId = 0
    private var adminId = 0

    private lateinit var actPaymentAccount: AutoCompleteTextView

    private var paymentAccounts =
        mutableListOf<SavedPaymentDetails>()

    private var selectedPaymentId = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_withdraw_fund)

        userId = intent.getIntExtra(
            "USER_ID",
            0
        )

        adminId = intent.getIntExtra(
            "ADMIN_ID",
            0
        )

        etAmount =
            findViewById(R.id.etAmount)

        ivQrPreview =
            findViewById(R.id.ivQrPreview)

        btnSubmit =
            findViewById(R.id.btnSubmit)

        tvSelectedUpi = findViewById(R.id.tvSelectedUpi)

        btnBack =
            findViewById(R.id.btnBack)

        actPaymentAccount =
            findViewById(R.id.actPaymentAccount)

        btnBack.setOnClickListener {

            finish()
        }

        loadPaymentAccounts()

        btnSubmit.setOnClickListener {

            val amount =
                etAmount.text
                    .toString()
                    .trim()

            if (userId == 0) {

                Toast.makeText(
                    this,
                    "Invalid User",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (amount.isEmpty()) {

                etAmount.error =
                    "Enter Amount"

                return@setOnClickListener
            }

            if (selectedPaymentId == 0) {

                Toast.makeText(
                    this,
                    "Please select a payment account",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            uploadWithdrawRequest(
                amount
            )
        }
    }

    private fun loadPaymentAccounts() {

        RetrofitClient.api
            .getPaymentAccounts(userId)
            .enqueue(object :
                Callback<List<SavedPaymentDetails>> {

                override fun onResponse(
                    call: Call<List<SavedPaymentDetails>>,
                    response: Response<List<SavedPaymentDetails>>
                ) {

                    if (!response.isSuccessful) {

                        Toast.makeText(
                            this@WithdrawFundActivity,
                            "Failed to load payment accounts",
                            Toast.LENGTH_SHORT
                        ).show()

                        return
                    }

                    paymentAccounts.clear()

                    paymentAccounts.addAll(
                        response.body() ?: listOf()
                    )

                    val names =
                        paymentAccounts.map {
                            it.account_name
                        }

                    val adapter =
                        ArrayAdapter(
                            this@WithdrawFundActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            names
                        )

                    actPaymentAccount.setAdapter(adapter)

                    actPaymentAccount.setOnItemClickListener { _, _, position, _ ->

                        val account =
                            paymentAccounts[position]

                        selectedPaymentId =
                            account.id

                        tvSelectedUpi.text = account.upi_id ?: "No UPI ID"

                        if (!account.qr_code.isNullOrEmpty()) {

                            val imageUrl =
                                "http://10.165.159.70:5000${account.qr_code}"

                            Glide.with(this@WithdrawFundActivity)
                                .load(imageUrl)
                                .into(ivQrPreview)

                        } else {

                            ivQrPreview.setImageDrawable(null)

                        }
                    }

                }

                override fun onFailure(
                    call: Call<List<SavedPaymentDetails>>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@WithdrawFundActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }
    private fun uploadWithdrawRequest(
        amount: String
    ) {

        val userBody =
            userId.toString()
                .toRequestBody(
                    "text/plain".toMediaTypeOrNull()
                )

        val adminBody =
            adminId.toString()
                .toRequestBody(
                    "text/plain".toMediaTypeOrNull()
                )

        val amountBody =
            amount.toRequestBody(
                "text/plain".toMediaTypeOrNull()
            )

        val paymentBody =
            selectedPaymentId.toString()
                .toRequestBody(
                    "text/plain".toMediaTypeOrNull()
                )

        btnSubmit.isEnabled = false

        RetrofitClient.api
            .withdrawFundRequest(
                userBody,
                adminBody,
                amountBody,
                paymentBody
            )
            .enqueue(object : Callback<ApiMessageResponse> {

                override fun onResponse(
                    call: Call<ApiMessageResponse>,
                    response: Response<ApiMessageResponse>
                ) {

                    btnSubmit.isEnabled = true

                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@WithdrawFundActivity,
                            response.body()?.message
                                ?: "Withdraw request submitted",
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






}
