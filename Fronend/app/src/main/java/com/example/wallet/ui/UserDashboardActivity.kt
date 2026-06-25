package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wallet.R
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class UserDashboardActivity : AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var tvProfile: ImageView
    private lateinit var tvWalletBalance: TextView

    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_dashboard)

        val userId = intent.getIntExtra("USER_ID", 0)

        swipeRefresh = findViewById(R.id.swipeRefresh)

        swipeRefresh.setOnRefreshListener {

            loadDashboard(userId)

        }

        loadDashboard(userId)

        tvUserName =
            findViewById(R.id.tvUserDisplayName)

        tvWalletBalance =
            findViewById(R.id.tvWalletAmount)

//        val userId =
//            intent.getIntExtra("USER_ID", 0)

        if (userId != 0) {
            loadDashboard(userId)
        }

        val tvProfile =
            findViewById<ImageView>(R.id.tvProfile)

        tvProfile.setOnClickListener {

            val intent = Intent(
                this,
                UserDetailsActivity::class.java
            )

            intent.putExtra(
                "USER_ID",
                userId
            )

            startActivity(intent)
        }
        val cardAddFundRequest =
            findViewById<CardView>(R.id.cardAddFundRequest)

        cardAddFundRequest.setOnClickListener {

            val intent = Intent(
                this,
                AddFundUserActivity::class.java
            )

            intent.putExtra(
                "USER_ID",
                userId
            )

            startActivity(intent)
        }
        val cardAllAccount =
            findViewById<CardView>(R.id.cardAllAccount)

        cardAllAccount.setOnClickListener {

            val intent = Intent(
                this,
                SavedPaymentAccountsActivity::class.java
            )


            startActivity(intent)
        }
        val cardWithdrawRequest =
            findViewById<CardView>(R.id.cardWithdrawRequest)

        cardWithdrawRequest.setOnClickListener {

            val intent = Intent(
                this,
                WithdrawFundActivity::class.java
            )

            intent.putExtra(
                "USER_ID",
                userId
            )

            startActivity(intent)
        }
        val cardHelpSupport =
            findViewById<CardView>(R.id.cardHelpSupport)

        cardHelpSupport.setOnClickListener {

            val intent = Intent(
                this,
                HelpSupportActivity::class.java
            )

            intent.putExtra(
                "USER_ID",
                userId
            )

            startActivity(intent)
        }
        val cardTransactionHistory =
            findViewById<CardView>(R.id.cardTransactionHistory)

        cardTransactionHistory.setOnClickListener {

            val intent = Intent(
                this,
                UserTransactionActivity::class.java
            )

            intent.putExtra(
                "USER_ID",
                userId
            )

            startActivity(intent)
        }
    }



    private fun loadDashboard(userId: Int) {

        Log.d("DASHBOARD", "USER_ID = $userId")

        lifecycleScope.launch {

            try {

                val dashboard =
                    RetrofitClient.api.getUserDashboard(userId)

                Log.d("DASHBOARD", "API RESPONSE = $dashboard")

                tvUserName.text = dashboard.full_name

                tvWalletBalance.text =
                    "₹${dashboard.wallet_balance ?: "0.00"}"

            } catch (e: Exception) {

                Log.e("DASHBOARD", "ERROR", e)

            } finally {

                swipeRefresh.isRefreshing = false

            }
        }
    }
}