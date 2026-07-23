package com.example.wallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.AdminRequestHistory
import android.graphics.Color
import java.text.SimpleDateFormat
import java.util.Locale

class AdminRequestAdapter(

    private val requests: List<AdminRequestHistory>

) : RecyclerView.Adapter<AdminRequestAdapter.ViewHolder>() {

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val tvUserName: TextView =
            view.findViewById(R.id.tvUserName)

        val tvRequestType: TextView =
            view.findViewById(R.id.tvRequestType)

        val tvAmount: TextView =
            view.findViewById(R.id.tvAmount)

        val tvStatus: TextView =
            view.findViewById(R.id.tvStatus)

        val tvDate: TextView =
            view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_admin_request,
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

        // Amount
        when (request.request_type.uppercase()) {

            "ADD" -> {

                holder.tvAmount.text =
                    "+ ₹${request.amount}"

                holder.tvAmount.setTextColor(
                    Color.parseColor("#2E7D32")
                )
            }

            else -> {

                holder.tvAmount.text =
                    "- ₹${request.amount}"

                holder.tvAmount.setTextColor(
                    Color.parseColor("#C62828")
                )
            }
        }

        // Request Type
        holder.tvRequestType.text =
            when (request.request_type.uppercase()) {

                "ADD" -> "ADD FUND"

                "WITHDRAW" -> "WITHDRAW"

                else -> request.request_type
            }

        // Request Type Badge Color
        when (request.request_type.uppercase()) {

            "ADD" -> {

                holder.tvRequestType.setTextColor(
                    Color.parseColor("#2E7D32")
                )

                holder.tvRequestType.setBackgroundResource(
                    R.drawable.bg_type_add
                )
            }

            "WITHDRAW" -> {

                holder.tvRequestType.setTextColor(
                    Color.parseColor("#C62828")
                )

                holder.tvRequestType.setBackgroundResource(
                    R.drawable.bg_type_withdraw
                )
            }
        }

        // Status
        holder.tvStatus.text =
            request.status.uppercase()

        when (request.status.uppercase()) {

            "APPROVED" -> {

                holder.tvStatus.setBackgroundResource(
                    R.drawable.bg_status_approved
                )

                holder.tvStatus.setTextColor(
                    Color.parseColor("#22C55E")
                )
            }

            "REJECTED" -> {

                holder.tvStatus.setBackgroundResource(
                    R.drawable.bg_status_rejected
                )

                holder.tvStatus.setTextColor(
                    Color.parseColor("#EF4444")
                )
            }

            "PENDING" -> {

                holder.tvStatus.setBackgroundResource(
                    R.drawable.bg_status_pending
                )

                holder.tvStatus.setTextColor(
                    Color.parseColor("#F59E0B")
                )
            }
        }

        holder.tvDate.text =
            formatDate(request.created_at)
    }
    private fun formatDate(dateString: String): String {

        return try {

            val input =
                SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
                    Locale.ENGLISH
                )

            val date =
                input.parse(dateString)

            val output =
                SimpleDateFormat(
                    "dd MMM yyyy • hh:mm a",
                    Locale.ENGLISH
                )

            output.format(date)

        } catch (e: Exception) {

            dateString
        }
    }
}