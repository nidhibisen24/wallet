package com.example.wallet.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wallet.R
import com.example.wallet.data.ForgotPasswordRequest
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var etMobile: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSend: Button
    private lateinit var tvBackLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etMobile = findViewById(R.id.etMobile)
        etEmail = findViewById(R.id.etEmail)
        btnSend = findViewById(R.id.btnSend)
        tvBackLogin = findViewById(R.id.tvBackLogin)

        btnSend.setOnClickListener {
            sendTemporaryPassword()
        }

        tvBackLogin.setOnClickListener {
            finish()
        }
    }

    private fun sendTemporaryPassword() {

        val mobile = etMobile.text.toString().trim()
        val email = etEmail.text.toString().trim()

        if (mobile.isEmpty()) {
            etMobile.error = "Enter mobile number"
            etMobile.requestFocus()
            return
        }

        if (email.isEmpty()) {
            etEmail.error = "Enter email"
            etEmail.requestFocus()
            return
        }

        btnSend.isEnabled = false
        btnSend.text = "Sending..."

        lifecycleScope.launch {

            try {

                val response = RetrofitClient.api.forgotPassword(
                    ForgotPasswordRequest(
                        mobile_number = mobile,
                        email = email
                    )
                )

                if (response.isSuccessful && response.body() != null) {

                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        response.body()!!.message,
                        Toast.LENGTH_LONG
                    ).show()

                    finish()

                } else {

                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        response.body()?.error ?: "Failed to send temporary password",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@ForgotPasswordActivity,
                    e.localizedMessage ?: "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()

            } finally {

                btnSend.isEnabled = true
                btnSend.text = "Send Temporary Password"

            }
        }
    }
}