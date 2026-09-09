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

    private val displayNames: MutableList<String>,

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

        val admin = admins[position]

        // Show random/display name only
        holder.tvName.text =
            displayNames[position]

        // Hide mobile number
        holder.tvMobile.visibility =
            View.GONE

        holder.itemView.setOnClickListener {

            // Real admin object is still used
            onClick(admin)
        }
    }

    fun updateNames(newNames: List<String>) {

        displayNames.clear()

        displayNames.addAll(newNames)

        notifyDataSetChanged()
    }
}