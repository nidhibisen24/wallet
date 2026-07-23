package com.example.wallet.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wallet.R
import com.example.wallet.data.ChangePasswordRequest
import com.example.wallet.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch


class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var etCurrentPassword: TextInputEditText
    private lateinit var etNewPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnChangePassword: MaterialButton
    private lateinit var tvCancel: TextView

    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        userId = intent.getIntExtra("USER_ID", 0)

        etCurrentPassword = findViewById(R.id.etCurrentPassword)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnChangePassword = findViewById(R.id.btnChangePassword)
        tvCancel = findViewById(R.id.tvCancel)

        btnChangePassword.setOnClickListener {
            changePassword()
        }

        tvCancel.setOnClickListener {
            finish()
        }
    }

    private fun changePassword() {

        val current = etCurrentPassword.text.toString().trim()
        val newPassword = etNewPassword.text.toString().trim()
        val confirm = etConfirmPassword.text.toString().trim()

        if (current.isEmpty()) {
            etCurrentPassword.error = "Enter current password"
            return
        }

        if (newPassword.isEmpty()) {
            etNewPassword.error = "Enter new password"
            return
        }

        if (confirm.isEmpty()) {
            etConfirmPassword.error = "Confirm password"
            return
        }

        if (newPassword != confirm) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        btnChangePassword.isEnabled = false

        lifecycleScope.launch {

            try {

                val response = RetrofitClient.api.changePassword(
                    ChangePasswordRequest(
                        user_id = userId,
                        current_password = current,
                        new_password = newPassword,
                        confirm_password = confirm
                    )
                )

                if (response.isSuccessful && response.body() != null) {

                    Toast.makeText(
                        this@ChangePasswordActivity,
                        response.body()!!.message,
                        Toast.LENGTH_LONG
                    ).show()

                    finish()

                } else {

                    Toast.makeText(
                        this@ChangePasswordActivity,
                        response.body()?.error ?: "Failed",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@ChangePasswordActivity,
                    e.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()

            } finally {

                btnChangePassword.isEnabled = true

            }
        }
    }
}