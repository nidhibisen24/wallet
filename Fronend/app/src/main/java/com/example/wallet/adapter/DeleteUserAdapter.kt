package com.example.wallet.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.User

class DeleteUserAdapter(
    private val users: List<User>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<DeleteUserAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvName: TextView =
            view.findViewById(R.id.tvUserName)

        val tvMobile: TextView =
            view.findViewById(R.id.tvMobile)

        val tvBalance: TextView =
            view.findViewById(R.id.tvBalance)

        val btnDelete: Button =
            view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_delete_user,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val user = users[position]

        holder.tvName.text =
            user.full_name

        holder.tvMobile.text =
            user.mobile_number

        holder.tvBalance.text =
            "₹${user.wallet_balance}"

        holder.btnDelete.setOnClickListener {
            onDeleteClick(user.id)
        }
    }
}