package com.example.wallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.PendingRequest

class PendingRequestAdapter(
    private val requests: List<PendingRequest>,
    private val onApprove: (Int) -> Unit,
    private val onReject: (Int) -> Unit
) : RecyclerView.Adapter<PendingRequestAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvRequestId: TextView =
            view.findViewById(R.id.tvRequestId)

        val tvAmount: TextView =
            view.findViewById(R.id.tvAmount)

        val tvType: TextView =
            view.findViewById(R.id.tvType)

        val btnApprove: Button =
            view.findViewById(R.id.btnApprove)

        val btnReject: Button =
            view.findViewById(R.id.btnReject)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_pending_request,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun getItemCount() = requests.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val request = requests[position]

        holder.tvRequestId.text =
            "Request ID: ${request.id}"

        holder.tvAmount.text =
            "Amount: ₹${request.amount}"

        holder.tvType.text =
            request.request_type

        holder.btnApprove.setOnClickListener {
            onApprove(request.id)
        }

        holder.btnReject.setOnClickListener {
            onReject(request.id)
        }
    }
}