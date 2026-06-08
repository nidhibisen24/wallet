package com.example.wallet.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.adapter.PendingRequestAdapter
import com.example.wallet.data.ApproveRequest
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class ApproveRequestsActivity : AppCompatActivity() {

    private lateinit var rvPendingRequests: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_approve_requests)

        rvPendingRequests =
            findViewById(R.id.rvPendingRequests)

        rvPendingRequests.layoutManager =
            LinearLayoutManager(this)

        loadRequests()
    }

    private fun loadRequests() {

        lifecycleScope.launch {

            val requests =
                RetrofitClient.api.getPendingRequests()

            rvPendingRequests.adapter =
                PendingRequestAdapter(
                    requests,
                    ::approveRequest,
                    ::rejectRequest
                )
        }
    }

    private fun approveRequest(id: Int) {

        lifecycleScope.launch {

            RetrofitClient.api.approveRequest(
                ApproveRequest(id)
            )

            loadRequests()
        }
    }

    private fun rejectRequest(id: Int) {

        lifecycleScope.launch {

            RetrofitClient.api.rejectRequest(
                ApproveRequest(id)
            )

            loadRequests()
        }
    }
}