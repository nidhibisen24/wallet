package com.example.wallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.Transaction

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

        holder.tvType.text =
            transaction.request_type

        holder.tvAmount.text =
            "₹${transaction.amount}"

        holder.tvStatus.text =
            transaction.status

        holder.tvDate.text =
            transaction.created_at
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}