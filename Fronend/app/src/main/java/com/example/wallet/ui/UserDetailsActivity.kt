package com.example.wallet.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wallet.R
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvMobile: TextView
    private lateinit var tvRole: TextView
    private lateinit var tvBalance: TextView
    private lateinit var tvTotalRequests: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_details)

        tvName = findViewById(R.id.tvName)
        tvMobile = findViewById(R.id.tvMobile)
        tvRole = findViewById(R.id.tvRole)
        tvBalance = findViewById(R.id.tvBalance)
        tvTotalRequests = findViewById(R.id.tvTotalRequests)

        val userId = intent.getIntExtra("USER_ID", 0)

        if (userId != 0) {
            loadUserDetails(userId)
        }
    }

    private fun loadUserDetails(userId: Int) {

        lifecycleScope.launch {

            try {

                val user =
                    RetrofitClient.api.getUserDetails(userId)

                tvName.text = user.full_name
                tvMobile.text = user.mobile_number
                tvRole.text = user.role
                tvBalance.text = "₹${user.wallet_balance}"
                tvTotalRequests.text =
                    user.total_requests.toString()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}