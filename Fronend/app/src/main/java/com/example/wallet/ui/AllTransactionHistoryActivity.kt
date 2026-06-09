package com.example.wallet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.adapter.TransactionHistoryAdapter
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class AllTransactionHistoryActivity : AppCompatActivity() {

    private lateinit var rvTransactions: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_all_transaction_history)

        rvTransactions =
            findViewById(R.id.rvTransactions)

        rvTransactions.layoutManager =
            LinearLayoutManager(this)

        loadTransactions()
    }

    private fun loadTransactions() {

        lifecycleScope.launch {

            try {

                val transactions =
                    RetrofitClient.api.getAllTransactions()

                rvTransactions.adapter =
                    TransactionHistoryAdapter(
                        transactions
                    )

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
}