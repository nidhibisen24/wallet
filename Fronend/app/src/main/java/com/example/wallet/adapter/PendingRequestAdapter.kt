package com.example.wallet.adapter

import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wallet.R
import com.example.wallet.data.PendingRequest

class PendingRequestAdapter(
    private val requests: List<PendingRequest>,
    private val onApprove: (Int) -> Unit,
    private val onReject: (Int) -> Unit
) : RecyclerView.Adapter<PendingRequestAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvUserName: TextView =
            view.findViewById(R.id.tvUserName)

        val tvMobile: TextView =
            view.findViewById(R.id.tvMobile)

        val tvDate: TextView =
            view.findViewById(R.id.tvDate)

        val tvAmount: TextView =
            view.findViewById(R.id.tvAmount)

        val tvType: TextView =
            view.findViewById(R.id.tvType)

        val tvUpiId: TextView =
            view.findViewById(R.id.tvUpiId)


        val btnApprove: Button =
            view.findViewById(R.id.btnApprove)

        val btnReject: Button =
            view.findViewById(R.id.btnReject)

        val btnViewQr: Button =
            view.findViewById(R.id.btnViewQr2)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_pending_request,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return requests.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val request = requests[position]

        holder.tvUserName.text =
            request.user_name

        holder.tvMobile.text =
            request.mobile_number

        holder.tvDate.text =
            request.created_at.substring(0, 10)

        holder.tvAmount.text =
            "₹${request.amount}"

        holder.tvType.text =
            request.request_type

        if (request.request_type == "WITHDRAW") {

            holder.tvUpiId.visibility =
                View.VISIBLE

            holder.btnViewQr.visibility =
                View.VISIBLE

            holder.tvUpiId.text =
                "UPI ID: ${request.upi_id}"

            holder.btnViewQr.setOnClickListener {

                if (!request.qr_code.isNullOrEmpty()) {

                    val dialog =
                        Dialog(holder.itemView.context)

                    dialog.setContentView(
                        R.layout.dialog_qr
                    )

                    val imgQr =
                        dialog.findViewById<ImageView>(
                            R.id.imgQr
                        )

                    Glide.with(holder.itemView.context)
                        .load(
                            "http://13.233.182.165${request.qr_code}"
                        )
                        .into(imgQr)

                    dialog.show()
                }
            }

        } else {

            holder.tvUpiId.visibility =
                View.GONE

            holder.btnViewQr.visibility =
                View.GONE
        }

        holder.btnApprove.setOnClickListener {
            onApprove(request.id)
        }

        holder.btnReject.setOnClickListener {
            onReject(request.id)
        }
    }
}