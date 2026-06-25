package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.adapter.SavedPaymentAdapter
import com.example.wallet.data.SavedPaymentDetails
import com.example.wallet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import com.google.android.material.button.MaterialButton

class SavedPaymentAccountsActivity : AppCompatActivity() {

    private lateinit var rvAccounts: RecyclerView
    private lateinit var adapter: SavedPaymentAdapter

    private lateinit var btnBack: CardView
    private lateinit var btnAddAccount: ImageView

    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_payment_account)

        val sharedPref = getSharedPreferences(
            "wallet_app",
            MODE_PRIVATE
        )

        userId = sharedPref.getInt(
            "user_id",
            0
        )

        rvAccounts =
            findViewById(R.id.rvAccounts)


        val btnAddAccount =
            findViewById<MaterialButton>(R.id.btnAddAccount)

        rvAccounts.layoutManager =
            LinearLayoutManager(this)

        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {

            finish()


        }

        btnAddAccount.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AddPaymentAccountActivity::class.java
                )
            )

        }

        loadAccounts()
    }

    override fun onResume() {
        super.onResume()

        loadAccounts()
    }

    private fun loadAccounts() {

        RetrofitClient.api
            .getPaymentAccounts(userId)
            .enqueue(object :
                Callback<List<SavedPaymentDetails>> {

                override fun onResponse(
                    call: Call<List<SavedPaymentDetails>>,
                    response: Response<List<SavedPaymentDetails>>
                ) {

                    if (response.isSuccessful) {

                        val accounts =
                            response.body()
                                ?: emptyList()

                        adapter =
                            SavedPaymentAdapter(
                                accounts,

                                onDefaultClick = { account ->

                                    Toast.makeText(
                                        this@SavedPaymentAccountsActivity,
                                        "Set Default API Coming Next",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                },

                                onEditClick = { account ->

                                    Toast.makeText(
                                        this@SavedPaymentAccountsActivity,
                                        "Edit API Coming Next",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                },

                                onDeleteClick = { account ->

                                    Toast.makeText(
                                        this@SavedPaymentAccountsActivity,
                                        "Delete API Coming Next",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            )

                        rvAccounts.adapter =
                            adapter

                    } else {

                        Toast.makeText(
                            this@SavedPaymentAccountsActivity,
                            "Unable to load accounts",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                override fun onFailure(
                    call: Call<List<SavedPaymentDetails>>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@SavedPaymentAccountsActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}