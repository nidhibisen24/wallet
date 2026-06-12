package com.example.wallet.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.TransactionHistory
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionHistoryAdapter(
    private val transactions: List<TransactionHistory>
) : RecyclerView.Adapter<TransactionHistoryAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val cardTransaction: CardView =
            itemView.findViewById(R.id.cardTransaction)

        val tvUserName: TextView =
            itemView.findViewById(R.id.tvUserName)

        val tvAmount: TextView =
            itemView.findViewById(R.id.tvAmount)

        val tvRequestType: TextView =
            itemView.findViewById(R.id.tvRequestType)

        val tvStatus: TextView =
            itemView.findViewById(R.id.tvStatus)

        val tvDate: TextView =
            itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_transaction_history,
                    parent,
                    false
                )

        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TransactionViewHolder,
        position: Int
    ) {

        val transaction =
            transactions[position]

        holder.tvUserName.text =
            transaction.user_name

        if (transaction.request_type.uppercase() == "ADD") {

            holder.tvAmount.text =
                "+ ₹${transaction.amount}"

            holder.tvAmount.setTextColor(
                Color.parseColor("#2E7D32")
            )

        } else {

            holder.tvAmount.text =
                "- ₹${transaction.amount}"

            holder.tvAmount.setTextColor(
                Color.parseColor("#C62828")
            )
        }

        holder.tvRequestType.text =
            transaction.request_type

        holder.tvStatus.text =
            transaction.status

        holder.tvDate.text =
            formatDate(transaction.created_at)

        // Card Color
        when (transaction.request_type.uppercase()) {

            "ADD" -> {

                holder.cardTransaction.setCardBackgroundColor(
                    Color.parseColor("#F1F8E9")
                )
            }

            "WITHDRAW" -> {

                holder.cardTransaction.setCardBackgroundColor(
                    Color.parseColor("#FEE2E2")
                )
            }

            else -> {

                holder.cardTransaction.setCardBackgroundColor(
                    Color.WHITE
                )
            }
        }

        // Status Badge
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
                    Color.parseColor("Gray")
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    private fun formatDate(dateString: String): String {

        return try {

            val inputFormat =
                SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
                    Locale.ENGLISH
                )

            val date =
                inputFormat.parse(dateString)

            val outputFormat =
                SimpleDateFormat(
                    "dd MMM yyyy • hh:mm a",
                    Locale.ENGLISH
                )

            outputFormat.format(date)

        } catch (e: Exception) {

            dateString
        }
    }
}