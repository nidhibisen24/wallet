package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.adapter.TransactionAdapter
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class UserDetailsActivity : AppCompatActivity() {


    private lateinit var tvName: TextView
    private lateinit var tvMobile: TextView
    private lateinit var tvRole: TextView
    private lateinit var tvBalance: TextView
    private lateinit var tvTotalRequests: TextView
    private lateinit var btnBack: CardView
    private lateinit var btnLogout: Button

    private lateinit var rvTransactions: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_details)

        tvName = findViewById(R.id.tvName)
        tvMobile = findViewById(R.id.tvMobile)
        tvRole = findViewById(R.id.tvRole)
        tvBalance = findViewById(R.id.tvBalance)
        tvTotalRequests = findViewById(R.id.tvTotalRequests)

        rvTransactions =
            findViewById(R.id.rvTransactions)

        rvTransactions.layoutManager =
            LinearLayoutManager(this)

        val userId =
            intent.getIntExtra("USER_ID", 0)

        if (userId != 0) {
            loadUserDetails(userId)
        }
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {

            finish()


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

                tvBalance.text =
                    "₹${user.wallet_balance}"

                tvTotalRequests.text =
                    user.total_requests.toString()

                val transactions =
                    RetrofitClient.api.getUserHistory(userId)

                rvTransactions.adapter =
                    TransactionAdapter(transactions)

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }


}
