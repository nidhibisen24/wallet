package com.example.wallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.User

class UserAdapter(
    private val users: List<User>,
    private val onUserClick: (Int) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val tvUserName: TextView =
            itemView.findViewById(R.id.tvUserName)

        private val tvMobile: TextView =
            itemView.findViewById(R.id.tvMobile)

        private val tvBalance: TextView =
            itemView.findViewById(R.id.tvBalance)

        fun bind(user: User) {

            tvUserName.text = user.full_name

            tvMobile.text = user.mobile_number

            tvBalance.text =
                "₹${user.wallet_balance ?: "0.00"}"

            itemView.setOnClickListener {
                onUserClick(user.id)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_user,
                parent,
                false
            )

        return UserViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }

}
