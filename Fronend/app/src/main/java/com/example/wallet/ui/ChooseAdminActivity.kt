package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    private lateinit var adminAdapter: AdminAdapter

    private val handler = Handler(Looper.getMainLooper())


    // Names that will only be displayed on screen
    private val randomNames = listOf(

        "Rahul Sharma",
        "Amit Kumar",
        "Rohan Singh",
        "Arjun Patel",
        "Karan Verma",
        "Vijay Kumar",
        "Ravi Sharma",
        "Ajay Singh",
        "Mohit Kumar",
        "Priya Sharma",
        "Ankit Verma",
        "Rohit Singh",
        "Deepak Kumar",
        "Manish Patel",
        "Akash Sharma",
        "Sumit Kumar",
        "Raj Verma",
        "Suresh Kumar",
        "Nikhil Sharma",
        "Vikas Singh"

    )


    // This runs every 20 seconds
    private val changeNamesRunnable = object : Runnable {

        override fun run() {

            // Create a random display name
            // for every admin card
            val newNames = List(
                adminAdapter.itemCount
            ) {

                randomNames.random()

            }

            // Update only displayed names
            adminAdapter.updateNames(
                newNames
            )


            // Run again after 20 seconds
            handler.postDelayed(
                this,
                20000
            )
        }
    }


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_choose_admin
        )


        rvAdmins =
            findViewById(
                R.id.rvAdmins
            )


        rvAdmins.layoutManager =
            LinearLayoutManager(
                this
            )


        val type =
            intent.getStringExtra(
                "TYPE"
            )


        val userId =
            intent.getIntExtra(
                "USER_ID",
                0
            )


        // Get real admins from API
        RetrofitClient.api
            .getAllAdmins()

            .enqueue(
                object : Callback<List<Admin>> {


                    override fun onResponse(

                        call: Call<List<Admin>>,

                        response: Response<List<Admin>>

                    ) {


                        if (
                            response.isSuccessful
                        ) {


                            val admins =
                                response.body()
                                    ?: emptyList()


                            // Create initial random names
                            val firstNames =
                                List(
                                    admins.size
                                ) {

                                    randomNames.random()

                                }


                            // Create adapter
                            adminAdapter =
                                AdminAdapter(

                                    admins,

                                    firstNames.toMutableList()

                                ) { admin ->


                                    when (
                                        type
                                    ) {


                                        "BUY" -> {


                                            val intent =
                                                Intent(

                                                    this@ChooseAdminActivity,

                                                    AddFundUserActivity::class.java

                                                )


                                            intent.putExtra(
                                                "USER_ID",
                                                userId
                                            )


                                            // REAL ADMIN ID
                                            intent.putExtra(
                                                "ADMIN_ID",
                                                admin.id
                                            )


                                            startActivity(
                                                intent
                                            )

                                        }


                                        "SELL" -> {


                                            val intent =
                                                Intent(

                                                    this@ChooseAdminActivity,

                                                    WithdrawFundActivity::class.java

                                                )


                                            intent.putExtra(
                                                "USER_ID",
                                                userId
                                            )


                                            // REAL ADMIN ID
                                            intent.putExtra(
                                                "ADMIN_ID",
                                                admin.id
                                            )


                                            startActivity(
                                                intent
                                            )

                                        }


                                        "CHAT" -> {


                                            val intent =
                                                Intent(

                                                    this@ChooseAdminActivity,

                                                    HelpSupportActivity::class.java

                                                )


                                            intent.putExtra(
                                                "USER_ID",
                                                userId
                                            )


                                            // REAL ADMIN ID
                                            intent.putExtra(
                                                "ADMIN_ID",
                                                admin.id
                                            )


                                            startActivity(
                                                intent
                                            )

                                        }

                                    }

                                }


                            // Set adapter
                            rvAdmins.adapter =
                                adminAdapter


                            // Start changing names
                            // after 20 seconds
                            handler.postDelayed(

                                changeNamesRunnable,

                                20000

                            )

                        }

                    }


                    override fun onFailure(

                        call: Call<List<Admin>>,

                        t: Throwable

                    ) {

                        // API failed

                    }

                }

            )

    }


    override fun onDestroy() {

        super.onDestroy()


        // Stop changing names when
        // Activity is destroyed
        handler.removeCallbacks(
            changeNamesRunnable
        )

    }

}