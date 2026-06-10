package com.example.wallet.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.adapter.ApprovedRequestAdapter
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class ApprovedRequestsActivity : AppCompatActivity() {

    private lateinit var rvApprovedRequests: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_approved_requests
        )

        rvApprovedRequests =
            findViewById(R.id.rvApprovedRequests)

        rvApprovedRequests.layoutManager =
            LinearLayoutManager(this)

        loadApprovedRequests()
    }

    private fun loadApprovedRequests() {

        lifecycleScope.launch {

            try {

                val requests =
                    RetrofitClient.api
                        .getApprovedRequests()

                rvApprovedRequests.adapter =
                    ApprovedRequestAdapter(
                        requests
                    )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}