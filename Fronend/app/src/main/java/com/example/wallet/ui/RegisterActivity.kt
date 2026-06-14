package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.wallet.R
import com.example.wallet.data.RegisterRequest
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etMobile: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        etName = findViewById(R.id.etName)
        etMobile = findViewById(R.id.etMobile)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            registerUser()
        }
        val tvLogin =
            findViewById<TextView>(
                R.id.tvLogin
            )

        tvLogin.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )
        }
    }

    private fun registerUser() {

        val name = etName.text.toString().trim()
        val mobile = etMobile.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (name.isEmpty() ||
            mobile.isEmpty() ||
            password.isEmpty() ||
            confirmPassword.isEmpty()
        ) {
            Toast.makeText(
                this,
                "Please fill all fields",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(
                this,
                "Passwords do not match",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        lifecycleScope.launch {

            try {

                val request = RegisterRequest(
                    full_name = name,
                    mobile_number = mobile,
                    password = password
                )

                RetrofitClient.api.registerUser(request)

                Toast.makeText(
                    this@RegisterActivity,
                    "User Registered Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            } catch (e: Exception) {

                Toast.makeText(
                    this@RegisterActivity,
                    "Registration Failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}