package com.example.wallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.ChatMessage

class AdminChatAdapter(
    private val messages: List<ChatMessage>
) : RecyclerView.Adapter<AdminChatAdapter.ViewHolder>() {

    companion object {
        private const val LEFT_MESSAGE = 0
        private const val RIGHT_MESSAGE = 1
    }

    class ViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        val tvSender: TextView =
            view.findViewById(R.id.tvSender)

        val tvMessage: TextView =
            view.findViewById(R.id.tvMessage)
    }

    override fun getItemViewType(position: Int): Int {

        return if (
            messages[position].sender_role.equals("ADMIN", true)
        ) {
            RIGHT_MESSAGE
        } else {
            LEFT_MESSAGE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val layoutId =
            if (viewType == RIGHT_MESSAGE)
                R.layout.item_message_right
            else
                R.layout.item_message_left

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    layoutId,
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

        holder.tvSender.text =
            if (msg.sender_role.equals("ADMIN", true))
                "ADMIN"
            else
                msg.sender_name

        holder.tvMessage.text =
            msg.message
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}