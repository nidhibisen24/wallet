package com.example.wallet.ui


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wallet.R
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var tvTotalUsers: TextView
    private lateinit var tvPendingRequests: TextView
    private lateinit var btnLogout: Button
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val sharedPref = getSharedPreferences(
            "wallet_app",
            MODE_PRIVATE
        )

        val userId = sharedPref.getInt(
            "user_id",
            0
        )
        swipeRefresh = findViewById(R.id.swipeRefresh)
        tvTotalUsers = findViewById(R.id.tvTotalUsers)
        tvPendingRequests = findViewById(R.id.tvPendingRequests)

        val role =
            intent.getStringExtra("ROLE")


        val cardCreateAdmin =
            findViewById<CardView>(
                R.id.cardCreateAdmin
            )


        if(role == "SUPER_ADMIN"){

            cardCreateAdmin.visibility =
                View.VISIBLE

        }else{

            cardCreateAdmin.visibility =
                View.GONE
        }
        loadDashboardData()
        val cardUserManagement =
            findViewById<CardView>(R.id.cardUserManagement)

        cardUserManagement.setOnClickListener {

            val intent = Intent(
                this,
                AllUsersActivity::class.java
            )

            intent.putExtra(
                "USER_ID",
                userId
            )

            startActivity(intent)
        }
        swipeRefresh.setOnRefreshListener {
            loadDashboardData()
        }
        btnLogout = findViewById(R.id.btnLogout)

        btnLogout.setOnClickListener {

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
        val btnCreateAdmin =
            findViewById<Button>(
                R.id.btnCreateAdmin
            )


        btnCreateAdmin.setOnClickListener {

            val sharedPref =
                getSharedPreferences(
                    "wallet_app",
                    MODE_PRIVATE
                )

            val userId =
                sharedPref.getInt(
                    "user_id",
                    0
                )

            val intent = Intent(
                this,
                CreateAdminActivity::class.java
            )

            intent.putExtra(
                "USER_ID",
                userId
            )

            startActivity(intent)
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

            val intent = Intent(
                this,
                DeleteUserActivity::class.java
            )

            intent.putExtra(
                "USER_ID",
                userId
            )

            startActivity(intent)
        }
        val btnAddBonus =
            findViewById<Button>(R.id.btnAddBonus)

        btnAddBonus.setOnClickListener {

            val intent = Intent(
                this,
                AddBonusUserActivity::class.java
            )

            intent.putExtra(
                "USER_ID",
                userId
            )

            startActivity(intent)
        }
        val btnChat =
            findViewById<Button>(R.id.btnChat)

        btnChat.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AdminChatRoomsActivity::class.java
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
        val btnApproved =
            findViewById<Button>(R.id.btnApproved)

        btnApproved.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ApprovedRequestsActivity::class.java
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
        swipeRefresh.isRefreshing = true
        lifecycleScope.launch {

            try {

                val sharedPref = getSharedPreferences(
                    "wallet_app",
                    MODE_PRIVATE
                )

                val adminId = sharedPref.getInt(
                    "user_id",
                    0
                )

                val response =
                    RetrofitClient.api.getAdminDashboard(adminId)

                tvTotalUsers.text = response.total_users.toString()

                tvPendingRequests.text = response.pending_requests.toString()

                tvTotalUsers.text =
                    response.total_users.toString()

                tvPendingRequests.text =
                    response.pending_requests.toString()

            } catch (e: Exception) {

                e.printStackTrace()
            }finally {

                swipeRefresh.isRefreshing = false

            }
        }
    }
}