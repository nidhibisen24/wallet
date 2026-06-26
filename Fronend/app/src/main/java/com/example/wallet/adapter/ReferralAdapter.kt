package com.example.wallet.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.data.ReferralHistory

class ReferralAdapter(
    private val referralList: List<ReferralHistory>
) : RecyclerView.Adapter<ReferralAdapter.ReferralViewHolder>() {

    class ReferralViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName: TextView =
            itemView.findViewById(R.id.tvName)

        val tvMobile: TextView =
            itemView.findViewById(R.id.tvMobile)

        val tvReward: TextView =
            itemView.findViewById(R.id.tvReward)

        val tvDate: TextView =
            itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReferralViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_referral,
                parent,
                false
            )

        return ReferralViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ReferralViewHolder,
        position: Int
    ) {

        val referral = referralList[position]

        holder.tvName.text =
            referral.name

        holder.tvMobile.text =
            referral.mobile_number

        holder.tvReward.text =
            "+₹${referral.reward}"

        holder.tvDate.text =
            formatDate(referral.created_at)
    }

    override fun getItemCount(): Int {

        return referralList.size
    }

    private fun formatDate(date: String): String {

        return try {

            date.substring(0, 10)

        } catch (e: Exception) {

            date
        }
    }
}