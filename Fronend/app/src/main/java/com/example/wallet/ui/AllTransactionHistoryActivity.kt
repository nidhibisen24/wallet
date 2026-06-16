package com.example.wallet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wallet.R
import com.example.wallet.adapter.TransactionHistoryAdapter
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class AllTransactionHistoryActivity : AppCompatActivity() {

    private lateinit var rvTransactions: RecyclerView
    private lateinit var btnBack: CardView

    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_all_transaction_history)

        rvTransactions =
            findViewById(R.id.rvTransactions)
        swipeRefresh = findViewById(R.id.swipeRefresh)

        rvTransactions.layoutManager =
            LinearLayoutManager(this)

        loadTransactions()
        swipeRefresh.setOnRefreshListener {
            loadTransactions()
        }



        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {

            finish()


        }
    }

    private fun loadTransactions() {

        swipeRefresh.isRefreshing = true
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
            finally {

                swipeRefresh.isRefreshing = false

            }

        }
    }
}