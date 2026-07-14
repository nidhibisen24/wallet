package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.adapter.AdminAdapter
import com.example.wallet.data.Admin
import com.example.wallet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseAdminActivity : AppCompatActivity() {

    private lateinit var rvAdmins: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_choose_admin
        )

        rvAdmins =
            findViewById(R.id.rvAdmins)

        rvAdmins.layoutManager =
            LinearLayoutManager(this)

        val type =
            intent.getStringExtra("TYPE")

        val userId =
            intent.getIntExtra(
                "USER_ID",
                0
            )

        RetrofitClient.api.getAllAdmins()

            .enqueue(object :
                Callback<List<Admin>> {

                override fun onResponse(
                    call: Call<List<Admin>>,
                    response: Response<List<Admin>>
                ) {

                    if(response.isSuccessful){

                        val admins =
                            response.body() ?: listOf()

                        rvAdmins.adapter =
                            AdminAdapter(admins){ admin ->

                                when(type){

                                    "BUY"->{

                                        val intent =
                                            Intent(
                                                this@ChooseAdminActivity,
                                                AddFundUserActivity::class.java
                                            )

                                        intent.putExtra("USER_ID",userId)
                                        intent.putExtra("ADMIN_ID",admin.id)

                                        startActivity(intent)
                                    }

                                    "SELL"->{

                                        val intent =
                                            Intent(
                                                this@ChooseAdminActivity,
                                                WithdrawFundActivity::class.java
                                            )

                                        intent.putExtra("USER_ID",userId)
                                        intent.putExtra("ADMIN_ID",admin.id)

                                        startActivity(intent)
                                    }

                                    "CHAT"->{

                                        val intent =
                                            Intent(
                                                this@ChooseAdminActivity,
                                                HelpSupportActivity::class.java
                                            )

                                        intent.putExtra("USER_ID",userId)
                                        intent.putExtra("ADMIN_ID",admin.id)

                                        startActivity(intent)
                                    }

                                }

                            }

                    }

                }

                override fun onFailure(
                    call: Call<List<Admin>>,
                    t: Throwable
                ) {

                }

            })

    }

}