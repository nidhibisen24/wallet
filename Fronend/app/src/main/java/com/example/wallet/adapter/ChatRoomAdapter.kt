package com.example.wallet.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.ChatRoom

class ChatRoomAdapter(
    private val rooms: List<ChatRoom>,
    private val onClick: (ChatRoom) -> Unit
) : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val tvUserName: TextView =
            view.findViewById(R.id.tvUserName)

        val tvLastMessage: TextView =
            view.findViewById(R.id.tvLastMessage)

        val imgProfile: ImageView =
            view.findViewById(R.id.imgProfile)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_chat_room,
                    parent,
                    false
                )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rooms.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val room = rooms[position]

        holder.tvUserName.text =
            room.full_name

        holder.tvLastMessage.text =
            room.mobile_number

        holder.itemView.setOnClickListener {
            onClick(room)
        }
    }
}