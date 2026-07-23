package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.adapter.AdminRequestAdapter
import com.example.wallet.adapter.TransactionAdapter
import com.example.wallet.data.BlockUserRequest
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Response
import com.example.wallet.data.BlockUserResponse
import retrofit2.Call

class UserDetailsActivity : AppCompatActivity() {


    private lateinit var tvName: TextView
    private lateinit var tvMobile: TextView
    private lateinit var tvRole: TextView
    private lateinit var tvBalance: TextView
    private lateinit var tvTotalRequests: TextView

    private lateinit var tvPendingRequests: TextView
    private lateinit var btnBack: CardView
    private lateinit var btnLogout: Button
    private lateinit var rvTransactions: RecyclerView
    private lateinit var cardChangePassword: CardView
    private lateinit var cardEditProfile: CardView
    private lateinit var tvEmail: TextView
    private var currentEmail = ""

    private var viewedUserId = 0

    private var isAdmin = false

    private var isBlocked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_details)

        tvName = findViewById(R.id.tvName)
        tvMobile = findViewById(R.id.tvMobile)
        tvRole = findViewById(R.id.tvRole)
        tvBalance = findViewById(R.id.tvBalance)
        tvTotalRequests = findViewById(R.id.tvTotalRequests)
        tvPendingRequests =
            findViewById(R.id.tvPendingRequests)
        tvEmail = findViewById(R.id.tvEmail)

        rvTransactions =
            findViewById(R.id.rvTransactions)

        rvTransactions.layoutManager =
            LinearLayoutManager(this)

        viewedUserId =
            intent.getIntExtra("USER_ID", 0)



        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {

            finish()


        }
        btnLogout = findViewById(R.id.btnLogout)
        val sharedPref =
            getSharedPreferences(
                "wallet_app",
                MODE_PRIVATE
            )

        val role =
            sharedPref.getString(
                "role",
                ""
            )
        val loggedInRole =
            sharedPref.getString(
                "role",
                ""
            )
        if (viewedUserId != 0) {

            if (loggedInRole == "SUPER_ADMIN") {

                loadProfile(viewedUserId)

            } else {

                loadUserDetails(viewedUserId)

            }
        }

        isAdmin =
            role == "ADMIN" ||
                    role == "SUPER_ADMIN"

        if (isAdmin) {

            btnLogout.text = "Loading..."

        } else {

            btnLogout.text = "Logout"

        }

        btnLogout.setOnClickListener {

            if (isAdmin) {

                toggleUserBlock()

            } else {

                val sharedPref =
                    getSharedPreferences(
                        "wallet_app",
                        MODE_PRIVATE
                    )

                sharedPref.edit()
                    .clear()
                    .apply()

                startActivity(
                    Intent(
                        this,
                        LoginActivity::class.java
                    )
                )

                finishAffinity()

            }
        }
        cardChangePassword = findViewById(R.id.cardChangePassword)
        cardEditProfile = findViewById(R.id.cardEditProfile)

        cardChangePassword.setOnClickListener {

            val intent = Intent(
                this,
                ChangePasswordActivity::class.java
            )

            intent.putExtra("USER_ID", viewedUserId)

            startActivity(intent)
        }
        cardEditProfile.setOnClickListener {

            val intent = Intent(
                this,
                EditProfileActivity::class.java
            )

            intent.putExtra("USER_ID", viewedUserId)
            intent.putExtra("FULL_NAME", tvName.text.toString())
            intent.putExtra("EMAIL", currentEmail)

            startActivity(intent)
        }
    }

    private fun loadUserDetails(userId: Int) {

        lifecycleScope.launch {

            try {

                val user =
                    RetrofitClient.api.getUserDetails(userId)

                tvName.text =
                    user.full_name

                tvMobile.text =
                    user.mobile_number

                tvRole.text =
                    user.role
                currentEmail = user.email
                tvEmail.text = user.email

                isBlocked = user.is_blocked

                updateBlockButton()

                tvBalance.text =
                    "₹${user.wallet_balance}"

                tvTotalRequests.text =
                    user.total_requests.toString()
                tvPendingRequests.text =
                    user.pending_requests.toString()

                val transactions =
                    RetrofitClient.api.getUserHistory(userId)

                rvTransactions.adapter =
                    TransactionAdapter(transactions)

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
    private fun toggleUserBlock() {

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

        val request =
            BlockUserRequest(
                admin_id = adminId,
                user_id = viewedUserId
            )

        RetrofitClient.api
            .toggleUserBlock(request)
            .enqueue(object : Callback<BlockUserResponse> {

                override fun onResponse(
                    call: Call<BlockUserResponse>,
                    response: Response<BlockUserResponse>
                ) {

                    if (response.isSuccessful && response.body() != null) {

                        val result = response.body()!!

                        isBlocked = result.is_blocked

                        updateBlockButton()

                        Toast.makeText(
                            this@UserDetailsActivity,
                            result.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        Toast.makeText(
                            this@UserDetailsActivity,
                            "Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<BlockUserResponse>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@UserDetailsActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
    private fun updateBlockButton() {

        if (!isAdmin) return

        if (isBlocked) {

            btnLogout.text = "Unblock"

            btnLogout.setBackgroundColor(
                ContextCompat.getColor(this, android.R.color.holo_green_dark)
            )

        } else {

            btnLogout.text = "Block"

            btnLogout.setBackgroundColor(
                ContextCompat.getColor(this, android.R.color.holo_red_dark)
            )
        }
    }

    private fun loadProfile(userId: Int) {

        lifecycleScope.launch {

            try {

                val user =
                    RetrofitClient.api.getUserDetails(userId)

                if (user.role == "ADMIN") {

                    loadAdminDetails(userId)

                } else {

                    loadUserDetails(userId)

                }

            } catch (e: Exception) {

                e.printStackTrace()

            }

        }

    }
    private fun loadAdminDetails(adminId: Int) {

        lifecycleScope.launch {

            try {

                val admin =
                    RetrofitClient.api.getAdminDetails(adminId)

                tvName.text = admin.full_name
                tvMobile.text = admin.mobile_number

                tvTotalRequests.text =
                    admin.total_requests.toString()

                tvPendingRequests.text =
                    admin.pending_requests.toString()

                rvTransactions.adapter =
                    AdminRequestAdapter(admin.history)

                tvEmail.text = admin.email
                tvRole.text = admin.role



                tvBalance.text = "₹${admin.wallet_balance}"

                isBlocked = admin.is_blocked

                updateBlockButton()

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    this@UserDetailsActivity,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}
