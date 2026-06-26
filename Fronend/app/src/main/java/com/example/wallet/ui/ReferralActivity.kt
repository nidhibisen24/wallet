package com.example.wallet.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wallet.data.MyReferralResponse
import com.example.wallet.R
import com.example.wallet.adapter.ReferralAdapter
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class ReferralActivity : AppCompatActivity() {

    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var tvReferralCode: TextView
    private lateinit var tvTotalReferral: TextView
    private lateinit var tvTotalBonus: TextView

    private lateinit var btnCopy: Button
    private lateinit var btnShare: Button

    private lateinit var recyclerReferral: RecyclerView

    private lateinit var adapter: ReferralAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_referral)

        val userId = intent.getIntExtra("USER_ID", 0)

        swipeRefresh = findViewById(R.id.swipeRefresh)

        tvReferralCode = findViewById(R.id.tvReferralCode)
        tvTotalReferral = findViewById(R.id.tvTotalReferral)
        tvTotalBonus = findViewById(R.id.tvTotalBonus)

        btnCopy = findViewById(R.id.btnCopy)
        btnShare = findViewById(R.id.btnShare)

        recyclerReferral = findViewById(R.id.recyclerReferral)

        recyclerReferral.layoutManager =
            LinearLayoutManager(this)

        swipeRefresh.setOnRefreshListener {

            loadReferral(userId)

        }

        btnCopy.setOnClickListener {

            val clipboard =
                getSystemService(Context.CLIPBOARD_SERVICE)
                        as ClipboardManager

            val clip = ClipData.newPlainText(
                "Referral Code",
                tvReferralCode.text.toString()
            )

            clipboard.setPrimaryClip(clip)

            Toast.makeText(
                this,
                "Referral code copied",
                Toast.LENGTH_SHORT
            ).show()

        }

        btnShare.setOnClickListener {

            val shareIntent = Intent(
                Intent.ACTION_SEND
            )

            shareIntent.type = "text/plain"

            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Join Pay Wallet using my referral code: ${tvReferralCode.text}"
            )

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Share Referral Code"
                )
            )

        }

        loadReferral(userId)
    }

    private fun loadReferral(userId: Int) {

        lifecycleScope.launch {

            try {

                val response: MyReferralResponse =
                    RetrofitClient.api.getMyReferral(userId)

                tvReferralCode.text =
                    response.my_referral_code

                tvTotalReferral.text =
                    response.total_referrals.toString()

                tvTotalBonus.text =
                    "₹${response.total_bonus}"

                adapter =
                    ReferralAdapter(response.referrals)

                recyclerReferral.adapter =
                    adapter

            } catch (e: Exception) {

                Toast.makeText(
                    this@ReferralActivity,
                    e.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()

            } finally {

                swipeRefresh.isRefreshing = false

            }

        }

    }

}