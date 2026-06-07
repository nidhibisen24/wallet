package com.example.wallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.User

class UserAdapter(
    private val users: List<User>
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvUserName: TextView =
            itemView.findViewById(R.id.tvUserName)

        val tvMobile: TextView =
            itemView.findViewById(R.id.tvMobile)

        val tvBalance: TextView =
            itemView.findViewById(R.id.tvBalance)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)

        return UserViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {

        val user = users[position]

        holder.tvUserName.text = user.full_name

        holder.tvMobile.text = user.mobile_number

        holder.tvBalance.text =
            "₹${user.wallet_balance ?: "0.00"}"
    }

    override fun getItemCount(): Int {
        return users.size
    }
}