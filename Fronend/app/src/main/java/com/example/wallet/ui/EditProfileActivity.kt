package com.example.wallet.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wallet.R
import com.example.wallet.data.UpdateProfileRequest
import com.example.wallet.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etFullName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var btnSave: MaterialButton
    private lateinit var tvCancel: TextView

    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        userId = intent.getIntExtra("USER_ID", 0)

        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        btnSave = findViewById(R.id.btnSave)
        tvCancel = findViewById(R.id.tvCancel)

        etFullName.setText(intent.getStringExtra("FULL_NAME"))
        etEmail.setText(intent.getStringExtra("EMAIL"))

        btnSave.setOnClickListener {
            updateProfile()
        }

        tvCancel.setOnClickListener {
            finish()
        }
    }

    private fun updateProfile() {

        val name = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()

        if (name.isEmpty()) {
            etFullName.error = "Enter full name"
            return
        }

        if (email.isEmpty()) {
            etEmail.error = "Enter email"
            return
        }

        btnSave.isEnabled = false

        lifecycleScope.launch {

            try {

                val response =
                    RetrofitClient.api.updateProfile(
                        UpdateProfileRequest(
                            user_id = userId,
                            full_name = name,
                            email = email
                        )
                    )

                if (response.isSuccessful && response.body() != null) {

                    Toast.makeText(
                        this@EditProfileActivity,
                        response.body()!!.message,
                        Toast.LENGTH_LONG
                    ).show()

                    finish()

                } else {

                    Toast.makeText(
                        this@EditProfileActivity,
                        response.body()?.error ?: "Update failed",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@EditProfileActivity,
                    e.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()

            } finally {

                btnSave.isEnabled = true

            }
        }
    }
}