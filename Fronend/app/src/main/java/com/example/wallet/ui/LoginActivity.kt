package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wallet.R
import com.example.wallet.data.LoginRequest
import com.example.wallet.data.LoginResponse
import com.example.wallet.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var etMobile: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref =
            getSharedPreferences(
                "wallet_app",
                MODE_PRIVATE
            )

        val isLoggedIn =
            sharedPref.getBoolean(
                "is_logged_in",
                false
            )

        if (isLoggedIn) {

            val role =
                sharedPref.getString(
                    "role",
                    ""
                )

            if (
                role == "ADMIN" ||
                role == "SUPER_ADMIN"
            ) {

                val intent = Intent(
                    this,
                    AdminDashboardActivity::class.java
                )

                intent.putExtra(
                    "ROLE",
                    role
                )

                startActivity(intent)

            } else {

                val intent = Intent(
                    this,
                    UserDashboardActivity::class.java
                )

                intent.putExtra(
                    "USER_ID",
                    sharedPref.getInt(
                        "user_id",
                        0
                    )
                )

                startActivity(intent)
            }

            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val tvRegister =
            findViewById<TextView>(
                R.id.tvRegister
            )

        tvRegister.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }

        etMobile =
            findViewById(R.id.etMobile)

        etPassword =
            findViewById(R.id.etPassword)

        btnLogin =
            findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {

            val mobile =
                etMobile.text.toString()

            val password =
                etPassword.text.toString()

            val request =
                LoginRequest(
                    mobile,
                    password
                )

            RetrofitClient.api.login(request)
                .enqueue(object :
                    Callback<LoginResponse> {

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {

                        if (
                            response.isSuccessful &&
                            response.body() != null
                        ) {

                            val user =
                                response.body()!!

                            val sharedPref =
                                getSharedPreferences(
                                    "wallet_app",
                                    MODE_PRIVATE
                                )

                            sharedPref.edit()
                                .putBoolean(
                                    "is_logged_in",
                                    true
                                )
                                .putInt(
                                    "user_id",
                                    user.user_id
                                )
                                .putString(
                                    "full_name",
                                    user.full_name
                                )
                                .putString(
                                    "role",
                                    user.role
                                )
                                .apply()

                            Toast.makeText(
                                this@LoginActivity,
                                "Login Success",
                                Toast.LENGTH_SHORT
                            ).show()

                            if (
                                user.role == "ADMIN" ||
                                user.role == "SUPER_ADMIN"
                            ) {

                                val intent = Intent(
                                    this@LoginActivity,
                                    AdminDashboardActivity::class.java
                                )

                                intent.putExtra(
                                    "ROLE",
                                    user.role
                                )

                                intent.putExtra(
                                    "USER_ID",
                                    user.user_id
                                )

                                startActivity(intent)

                            } else {

                                val intent =
                                    Intent(
                                        this@LoginActivity,
                                        UserDashboardActivity::class.java
                                    )

                                intent.putExtra(
                                    "USER_ID",
                                    user.user_id
                                )

                                startActivity(intent)
                            }

                            finish()

                        } else {

                            Toast.makeText(
                                this@LoginActivity,
                                "Invalid Mobile Number or Password",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<LoginResponse>,
                        t: Throwable
                    ) {

                        Toast.makeText(
                            this@LoginActivity,
                            t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }
}

