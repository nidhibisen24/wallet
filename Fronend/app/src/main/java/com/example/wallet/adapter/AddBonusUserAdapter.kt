package com.example.wallet.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.User

class AddBonusUserAdapter(
    private val users: List<User>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<AddBonusUserAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvName: TextView =
            view.findViewById(R.id.tvUserName)

        val tvMobile: TextView =
            view.findViewById(R.id.tvMobile)

        val tvBalance: TextView =
            view.findViewById(R.id.tvBalance)

        val btnAddBonus: Button =
            view.findViewById(R.id.btnAddBonus)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_list_bonus_user,
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

        holder.btnAddBonus.setOnClickListener {
            onDeleteClick(user.id)
        }
    }
}