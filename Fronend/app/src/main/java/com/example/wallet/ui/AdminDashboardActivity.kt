package com.example.wallet.ui


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wallet.R
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch
import android.content.Intent
import android.widget.Button
import androidx.cardview.widget.CardView

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var tvTotalUsers: TextView
    private lateinit var tvPendingRequests: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        tvTotalUsers = findViewById(R.id.tvTotalUsers)
        tvPendingRequests = findViewById(R.id.tvPendingRequests)

        loadDashboardData()
        val cardUserManagement =
            findViewById<CardView>(R.id.cardUserManagement)

        cardUserManagement.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AllUsersActivity::class.java
                )
            )
        }
        val cardPendingRequest =
            findViewById<CardView>(R.id.cardPendingRequest)

        cardPendingRequest.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ApproveRequestsActivity::class.java
                )
            )
        }
        val btnRemoveUser =
            findViewById<Button>(R.id.btnRemoveUser)

        btnRemoveUser.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    DeleteUserActivity::class.java
                )
            )
        }
        val btnApprove =
            findViewById<Button>(R.id.btnApprove)

        btnApprove.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ApproveRequestsActivity::class.java
                )
            )
        }
        val btnViewHistory =
            findViewById<Button>(R.id.btnViewHistory)

        btnViewHistory.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AllTransactionHistoryActivity::class.java
                )
            )
        }
        val btnUpdateQr =
            findViewById<Button>(R.id.btnUpdateQr)

        btnUpdateQr.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    QrManagementActivity::class.java
                )
            )
        }
        val btnRegisterUser =
            findViewById<Button>(R.id.btnRegisterUser)

        btnRegisterUser.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AddUserActivity::class.java
                )
            )
        }
    }

    private fun loadDashboardData() {

        lifecycleScope.launch {

            try {

                val response = RetrofitClient.api.getAdminDashboard()

                tvTotalUsers.text = response.total_users.toString()

                tvPendingRequests.text = response.pending_requests.toString()

                tvTotalUsers.text =
                    response.total_users.toString()

                tvPendingRequests.text =
                    response.pending_requests.toString()

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
}