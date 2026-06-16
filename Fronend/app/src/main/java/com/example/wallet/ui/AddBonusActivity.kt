package com.example.wallet.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wallet.R
import com.example.wallet.data.AddBonusRequest
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class AddBonusActivity : AppCompatActivity() {

    private lateinit var etBonusAmount: EditText
    private lateinit var btnAddBonus: Button

    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_bonus)

        userId = intent.getIntExtra(
            "USER_ID",
            0
        )

        etBonusAmount =
            findViewById(R.id.etBonusAmount)

        btnAddBonus =
            findViewById(R.id.btnAddBonus)

        btnAddBonus.setOnClickListener {

            addBonus()
        }
    }

    private fun addBonus() {

        val amountText =
            etBonusAmount.text.toString().trim()

        if (amountText.isEmpty()) {

            etBonusAmount.error =
                "Enter bonus amount"

            return
        }

        lifecycleScope.launch {

            try {

                val response =
                    RetrofitClient.api.addBonus(

                        AddBonusRequest(
                            user_id = userId,
                            bonus_amount = amountText.toInt()
                        )
                    )

                Toast.makeText(
                    this@AddBonusActivity,
                    response.message,
                    Toast.LENGTH_SHORT
                ).show()

                if (response.status) {

                    finish()
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@AddBonusActivity,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}