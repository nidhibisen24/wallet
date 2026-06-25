package com.example.wallet.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wallet.R
import com.example.wallet.data.SavedPaymentDetails
import com.google.android.material.button.MaterialButton

class SavedPaymentAdapter(

    private val accounts: List<SavedPaymentDetails>,

    private val onDefaultClick: (SavedPaymentDetails) -> Unit,

    private val onEditClick: (SavedPaymentDetails) -> Unit,

    private val onDeleteClick: (SavedPaymentDetails) -> Unit

) : RecyclerView.Adapter<SavedPaymentAdapter.PaymentViewHolder>() {


    class PaymentViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val cardAccount: CardView =
            itemView.findViewById(R.id.cardAccount)

        val tvAccountName: TextView =
            itemView.findViewById(R.id.tvAccountName)

        val tvDefault: TextView =
            itemView.findViewById(R.id.tvDefault)

        val tvUpi: TextView =
            itemView.findViewById(R.id.tvUpi)

        val imgQr: ImageView =
            itemView.findViewById(R.id.imgQr)

        val btnDefault: MaterialButton =
            itemView.findViewById(R.id.btnDefault)

        val btnEdit: MaterialButton =
            itemView.findViewById(R.id.btnEdit)

        val btnDelete: MaterialButton =
            itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_saved_payment,
                    parent,
                    false
                )

        return PaymentViewHolder(view)
    }
    override fun onBindViewHolder(
        holder: PaymentViewHolder,
        position: Int
    ) {

        val account = accounts[position]

        // Account Name
        holder.tvAccountName.text =
            account.account_name

        // UPI ID
        holder.tvUpi.text =
            account.upi_id ?: "No UPI ID"

        // Default Badge
        if (account.is_default) {

            holder.tvDefault.visibility =
                View.VISIBLE

            holder.btnDefault.text =
                "Default"

            holder.btnDefault.isEnabled =
                false

        } else {

            holder.tvDefault.visibility =
                View.GONE

            holder.btnDefault.text =
                "Set Default"

            holder.btnDefault.isEnabled =
                true
        }

        // QR Image
        if (!account.qr_code.isNullOrEmpty()) {

            holder.imgQr.visibility =
                View.VISIBLE

            Glide.with(holder.itemView.context)
                .load("http://10.239.70.70:5000${account.qr_code}")
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgQr)

        } else {

            holder.imgQr.visibility =
                View.GONE
        }

        // Set Default Button
        holder.btnDefault.setOnClickListener {

            onDefaultClick(account)
        }

        // Edit Button
        holder.btnEdit.setOnClickListener {

            onEditClick(account)
        }

        // Delete Button
        holder.btnDelete.setOnClickListener {

            onDeleteClick(account)
        }

        // Card Click
        holder.cardAccount.setOnClickListener {

            // Reserved for future use
        }
    }

    override fun getItemCount(): Int {
        return accounts.size
    }
}