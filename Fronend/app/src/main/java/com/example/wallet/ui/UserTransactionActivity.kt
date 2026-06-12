package com.example.wallet.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.adapter.TransactionAdapter
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class UserTransactionActivity : AppCompatActivity() {

    private lateinit var rvTransactions: RecyclerView
    private lateinit var btnBack: CardView

    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_user_transaction
        )

        userId =
            intent.getIntExtra(
                "USER_ID",
                0
            )

        rvTransactions =
            findViewById(R.id.rvTransactions)

        rvTransactions.layoutManager =
            LinearLayoutManager(this)

        loadTransactions()

        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {

            finish()


        }
    }

    private fun loadTransactions() {

        lifecycleScope.launch {

            try {

                val transactions =
                    RetrofitClient.api
                        .getUserHistory(userId)

                rvTransactions.adapter =
                    TransactionAdapter(
                        transactions
                    )

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
}