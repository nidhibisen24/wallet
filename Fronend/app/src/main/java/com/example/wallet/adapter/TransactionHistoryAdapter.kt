package com.example.wallet.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.TransactionHistory

class TransactionHistoryAdapter(
    private val transactions: List<TransactionHistory>
) : RecyclerView.Adapter<TransactionHistoryAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

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

        val view = LayoutInflater.from(parent.context)
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

        val transaction = transactions[position]

        holder.tvUserName.text =
            transaction.user_name

        holder.tvAmount.text =
            "₹${transaction.amount}"

        holder.tvRequestType.text =
            transaction.request_type

        holder.tvStatus.text =
            transaction.status

        holder.tvDate.text =
            transaction.created_at
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}