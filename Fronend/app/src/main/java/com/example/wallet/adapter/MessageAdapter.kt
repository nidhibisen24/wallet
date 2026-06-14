package com.example.wallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.ChatMessage

class MessageAdapter(
    private val messages: List<ChatMessage>
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    class ViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        val tvMessage: TextView =
            view.findViewById(R.id.tvMessage)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_message,
                    parent,
                    false
                )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val msg = messages[position]

        holder.tvMessage.text =
            "${msg.sender_name}\n${msg.message}"
    }

    override fun getItemCount() =
        messages.size
}