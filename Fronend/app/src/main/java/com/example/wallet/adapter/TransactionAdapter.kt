package com.example.wallet.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.Transaction
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class TransactionAdapter(
    private val transactions: List<Transaction>
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val tvType: TextView =
            itemView.findViewById(R.id.tvType)

        val tvAmount: TextView =
            itemView.findViewById(R.id.tvAmount)

        val tvStatus: TextView =
            itemView.findViewById(R.id.tvStatus)

        val tvDate: TextView =
            itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_transaction,
                parent,
                false
            )

        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TransactionViewHolder,
        position: Int
    ) {

        val transaction = transactions[position]

        // Transaction Type
        holder.tvType.text =
            if (transaction.request_type.equals("ADD", true))
                "ADD FUND"
            else
                "WITHDRAW"

        // Amount Color & Sign
        if (transaction.request_type.equals("ADD", true)) {

            holder.tvAmount.text =
                "+ ₹${transaction.amount}"

            holder.tvAmount.setTextColor(
                Color.parseColor("#16A34A")
            )

        } else {

            holder.tvAmount.text =
                "- ₹${transaction.amount}"

            holder.tvAmount.setTextColor(
                Color.parseColor("#DC2626")
            )
        }

        // Status
        holder.tvStatus.text =
            transaction.status.uppercase()

        when (transaction.status.uppercase()) {

            "APPROVED", "SUCCESS" -> {

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

        // Date
        holder.tvDate.text =
            formatDate(transaction.created_at)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    private fun formatDate(dateString: String): String {

        return try {

            val inputFormat = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
                Locale.ENGLISH
            )

            inputFormat.timeZone =
                TimeZone.getTimeZone("UTC")

            val date =
                inputFormat.parse(dateString)

            val outputFormat =
                SimpleDateFormat(
                    "dd MMM yyyy • hh:mm a",
                    Locale.ENGLISH
                )

            outputFormat.format(date!!)

        } catch (e: Exception) {

            dateString
        }
    }
}