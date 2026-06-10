package com.example.wallet.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.ApprovedRequest

class ApprovedRequestAdapter(
    private val requests: List<ApprovedRequest>
) : RecyclerView.Adapter<ApprovedRequestAdapter.ViewHolder>() {

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val tvUserName: TextView =
            view.findViewById(R.id.tvUserName)

        val tvMobile: TextView =
            view.findViewById(R.id.tvMobile)

        val tvDate: TextView =
            view.findViewById(R.id.tvDate)

        val tvAmount: TextView =
            view.findViewById(R.id.tvAmount)

        val tvType: TextView =
            view.findViewById(R.id.tvType)

        val tvStatus: TextView =
            view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_approved_request,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun getItemCount() =
        requests.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val request = requests[position]

        holder.tvUserName.text =
            request.user_name

        holder.tvMobile.text =
            request.mobile_number

        holder.tvDate.text =
            request.created_at.take(10)

        holder.tvAmount.text =
            "₹${request.amount}"

        holder.tvType.text =
            request.request_type

        holder.tvStatus.text =
            request.status
    }
}