package com.example.wallet.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.wallet.R
import com.example.wallet.data.RegisterRequest
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class AddUserActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etMobileNumber: EditText
    private lateinit var etUserPassword: EditText
    private lateinit var btnAddUser: Button
    private lateinit var btnBack: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        etFullName = findViewById(R.id.etFullName)
        etMobileNumber = findViewById(R.id.etMobileNumber)
        etUserPassword = findViewById(R.id.etUserPassword)
        btnAddUser = findViewById(R.id.btnAddUser)

        btnAddUser.setOnClickListener {
            addUser()
        }
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {

            finish()


        }
    }

    private fun addUser() {

        val fullName = etFullName.text.toString().trim()
        val mobile = etMobileNumber.text.toString().trim()
        val password = etUserPassword.text.toString().trim()

        if (fullName.isEmpty() ||
            mobile.isEmpty() ||
            password.isEmpty()
        ) {

            Toast.makeText(
                this,
                "Please fill all fields",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        lifecycleScope.launch {

            try {

                val request = RegisterRequest(
                    full_name = fullName,
                    mobile_number = mobile,
                    password = password
                )

                RetrofitClient.api.registerUser(request)

                Toast.makeText(
                    this@AddUserActivity,
                    "User Added Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            } catch (e: Exception) {

                Toast.makeText(
                    this@AddUserActivity,
                    "Failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}