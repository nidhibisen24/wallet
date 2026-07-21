package com.example.wallet.ui


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wallet.R
import com.example.wallet.data.CreateAdminRequest
import com.example.wallet.data.CreateAdminResponse
import com.example.wallet.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class CreateAdminActivity : AppCompatActivity() {


    private lateinit var etFullName: TextInputEditText
    private lateinit var etMobile: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnCreate: MaterialButton



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_create_admin
        )


        val superAdminId =
            intent.getIntExtra(
                "USER_ID",
                0
            )


        etFullName =
            findViewById(R.id.etFullName)


        etMobile =
            findViewById(R.id.etMobile)


        etPassword =
            findViewById(R.id.etPassword)
        btnCreate =
            findViewById(R.id.btnCreate)



        btnCreate.setOnClickListener {

            val fullName =
                etFullName.text.toString().trim()

            val mobile =
                etMobile.text.toString().trim()

            val password =
                etPassword.text.toString().trim()

            if (fullName.isEmpty() ||
                mobile.isEmpty() ||
                password.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Mobile number must be exactly 10 digits
            if (mobile.length != 10 || !mobile.all { it.isDigit() }) {

                etMobile.error =
                    "Enter a valid 10-digit mobile number"

                etMobile.requestFocus()

                return@setOnClickListener
            }

            val request =
                CreateAdminRequest(

                    super_admin_id = superAdminId,

                    full_name = fullName,

                    mobile_number = mobile,

                    password = password
                )

            RetrofitClient.api.createAdmin(request)
                .enqueue(object : Callback<CreateAdminResponse> {

                    override fun onResponse(
                        call: Call<CreateAdminResponse>,
                        response: Response<CreateAdminResponse>
                    ) {

                        if (response.isSuccessful) {

                            Toast.makeText(
                                this@CreateAdminActivity,
                                "Admin Created",
                                Toast.LENGTH_SHORT
                            ).show()

                            finish()

                        } else {

                            Toast.makeText(
                                this@CreateAdminActivity,
                                "Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<CreateAdminResponse>,
                        t: Throwable
                    ) {

                        Toast.makeText(
                            this@CreateAdminActivity,
                            t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}