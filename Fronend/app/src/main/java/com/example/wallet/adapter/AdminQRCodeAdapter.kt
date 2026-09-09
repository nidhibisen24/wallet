package com.example.wallet.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.wallet.R
import com.example.wallet.data.AdminQRCode

class AdminQRCodeAdapter(
    private val qrCodes: List<AdminQRCode>,
    private val listener: OnQrActionListener
) : RecyclerView.Adapter<AdminQRCodeAdapter.ViewHolder>() {

    interface OnQrActionListener {
        fun onActivate(qr: AdminQRCode)
    }

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val ivQrCode: ImageView =
            view.findViewById(R.id.ivQrCode)

        val tvQrName: TextView =
            view.findViewById(R.id.tvQrName)

        val tvStatus: TextView =
            view.findViewById(R.id.tvStatus)

        val btnActivate: Button =
            view.findViewById(R.id.btnActivate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_admin_qr,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val qr = qrCodes[position]

        holder.ivQrCode.load(
            qr.image_url ?: qr.image
        )

        holder.tvQrName.text =
            "QR Code #${qr.id}"

        if (qr.is_active) {

            // Active QR
            holder.tvStatus.text = "ACTIVE"
            holder.tvStatus.setTextColor(
                Color.parseColor("#247B2D")
            )

            holder.btnActivate.text = "Active"

            holder.btnActivate.backgroundTintList =
                ColorStateList.valueOf(
                    Color.parseColor("#D9F2D2")
                )

            holder.btnActivate.setTextColor(
                Color.parseColor("#247B2D")
            )

            // Disable button for active QR
            holder.btnActivate.setOnClickListener(null)

        } else {

            // Inactive QR
            holder.tvStatus.text = "INACTIVE"
            holder.tvStatus.setTextColor(
                Color.parseColor("#247B2D")
            )

            holder.btnActivate.text = "Use This QR"

            holder.btnActivate.backgroundTintList =
                ColorStateList.valueOf(
                    Color.parseColor("#2E7D32")
                )

            holder.btnActivate.setTextColor(
                Color.WHITE
            )

            holder.btnActivate.setOnClickListener {

                // Tell Activity to activate this QR
                listener.onActivate(qr)
            }
        }
    }

    override fun getItemCount(): Int {
        return qrCodes.size
    }
}