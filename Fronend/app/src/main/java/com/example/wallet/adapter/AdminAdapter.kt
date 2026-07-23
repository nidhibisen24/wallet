package com.example.wallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.Admin

class AdminAdapter(

    private val admins: List<Admin>,

    private val onClick: (Admin) -> Unit

) : RecyclerView.Adapter<AdminAdapter.ViewHolder>() {

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val tvName: TextView =
            view.findViewById(R.id.tvAdminName)

        val tvMobile: TextView =
            view.findViewById(R.id.tvAdminMobile)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)

            .inflate(
                R.layout.item_admin,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun getItemCount() =
        admins.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val admin =
            admins[position]

        holder.tvName.text =
            admin.full_name

        holder.tvMobile.text =
            admin.mobile_number
        holder.tvMobile.visibility = View.GONE



        holder.itemView.setOnClickListener {

            onClick(admin)

        }
    }
}