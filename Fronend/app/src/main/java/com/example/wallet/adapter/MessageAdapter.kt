package com.example.wallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wallet.R
import com.example.wallet.data.ChatMessage

class MessageAdapter(
    private val messages: List<ChatMessage>
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

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

        val ivMessageImage: ImageView =
            view.findViewById(R.id.ivMessageImage)
    }

    override fun getItemViewType(position: Int): Int {

        val message = messages[position]

        android.util.Log.d(
            "CHAT_ROLE",
            "${message.sender_role} : ${message.message}"
        )

        return if (
            message.sender_role.equals("ADMIN", true)
        ) {
            LEFT_MESSAGE
        } else {
            RIGHT_MESSAGE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val layoutId =
            if (viewType == LEFT_MESSAGE)
                R.layout.item_message_left
            else
                R.layout.item_message_right

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

        // IMAGE MESSAGE
        if (
            msg.message_type.equals("image", true) &&
            !msg.image.isNullOrEmpty()
        ) {

            holder.tvMessage.visibility = View.GONE
            holder.ivMessageImage.visibility = View.VISIBLE

            Glide.with(holder.itemView.context)
                .load(msg.image)
                .into(holder.ivMessageImage)

        } else {

            // TEXT MESSAGE
            holder.ivMessageImage.visibility = View.GONE
            holder.tvMessage.visibility = View.VISIBLE

            holder.tvMessage.text =
                msg.message ?: ""
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}