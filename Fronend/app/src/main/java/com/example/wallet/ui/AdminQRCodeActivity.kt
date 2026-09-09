package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.adapter.AdminQRCodeAdapter
import com.example.wallet.data.ActivateQrRequest
import com.example.wallet.data.AdminQRCode
import com.example.wallet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminQRCodeActivity : AppCompatActivity() {

    private lateinit var rvQrCodes: RecyclerView
    private lateinit var btnUploadQr: Button

    private val qrList = mutableListOf<AdminQRCode>()

    private lateinit var adapter: AdminQRCodeAdapter

    private var adminId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_admin_qrcode)

        val sharedPref =
            getSharedPreferences(
                "wallet_app",
                MODE_PRIVATE
            )

        adminId = sharedPref.getInt(
            "user_id",
            0
        )

        rvQrCodes = findViewById(R.id.rvQrCodes)
        btnUploadQr = findViewById(R.id.btnUploadQr)

        adapter = AdminQRCodeAdapter(
            qrList,
            object : AdminQRCodeAdapter.OnQrActionListener {

                override fun onActivate(qr: AdminQRCode) {
                    activateQr(qr.id)
                }
            }
        )

        rvQrCodes.layoutManager =
            LinearLayoutManager(this)

        rvQrCodes.adapter = adapter

        btnUploadQr.setOnClickListener {

            // Open your existing QR upload activity
            val intent =
                Intent(
                    this,
                    QrManagementActivity::class.java
                )

            startActivity(intent)
        }

        loadQrCodes()
    }

    private fun loadQrCodes() {

        RetrofitClient.api
            .getAdminQrCodes(adminId)
            .enqueue(object : Callback<List<AdminQRCode>> {

                override fun onResponse(
                    call: Call<List<AdminQRCode>>,
                    response: Response<List<AdminQRCode>>
                ) {

                    if (response.isSuccessful) {

                        qrList.clear()

                        response.body()?.let {
                            qrList.addAll(it)
                        }

                        adapter.notifyDataSetChanged()

                    } else {

                        Toast.makeText(
                            this@AdminQRCodeActivity,
                            "Failed to load QR codes",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<AdminQRCode>>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@AdminQRCodeActivity,
                        t.localizedMessage ?: "Network Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun activateQr(qrId: Int) {

        val request =
            ActivateQrRequest(
                admin_id = adminId
            )

        RetrofitClient.api
            .activateQrCode(
                qrId,
                request
            )
            .enqueue(object : Callback<Void> {

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {

                    if (response.isSuccessful) {

                        Toast.makeText(
                            this@AdminQRCodeActivity,
                            "QR Code activated",
                            Toast.LENGTH_SHORT
                        ).show()

                        loadQrCodes()

                    } else {

                        Toast.makeText(
                            this@AdminQRCodeActivity,
                            "Failed to activate QR",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<Void>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@AdminQRCodeActivity,
                        t.localizedMessage ?: "Network Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    override fun onResume() {
        super.onResume()

        loadQrCodes()
    }
}