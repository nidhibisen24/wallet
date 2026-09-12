package com.example.wallet.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.UserChatRoom

class UserChatRoomAdapter(
    private val rooms: List<UserChatRoom>,
    private val onClick: (UserChatRoom) -> Unit
) : RecyclerView.Adapter<UserChatRoomAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvAdminName: TextView =
            view.findViewById(R.id.tvAdminName)

        val tvAdminPhone: TextView =
            view.findViewById(R.id.tvAdminPhone)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_user_chat_room,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val room = rooms[position]

        holder.tvAdminName.text =
            room.full_name

        holder.tvAdminPhone.text =
            room.mobile_number

        holder.itemView.setOnClickListener {
            onClick(room)
        }
    }

    override fun getItemCount(): Int {
        return rooms.size
    }
}