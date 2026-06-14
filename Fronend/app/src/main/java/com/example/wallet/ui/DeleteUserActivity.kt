package com.example.wallet.ui

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.adapter.DeleteUserAdapter
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class DeleteUserActivity : AppCompatActivity() {

    private lateinit var rvUsers: RecyclerView
    private lateinit var btnBack: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_delete_user)

        rvUsers = findViewById(R.id.rvUsers)

        rvUsers.layoutManager =
            LinearLayoutManager(this)

        loadUsers()
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {

            finish()


        }
    }

    private fun loadUsers() {

        lifecycleScope.launch {

            try {

                val users =
                    RetrofitClient.api.getAllUsers()

                rvUsers.adapter =
                    DeleteUserAdapter(users) { userId ->

                        AlertDialog.Builder(this@DeleteUserActivity)
                            .setTitle("Delete User")
                            .setMessage("Are you sure?")
                            .setPositiveButton("Delete") { _, _ ->
                                deleteUser(userId)
                            }
                            .setNegativeButton("Cancel", null)
                            .show()
                    }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun deleteUser(userId: Int) {

        lifecycleScope.launch {

            try {

                val response =
                    RetrofitClient.api.deleteUser(userId)

                if (response.isSuccessful) {

                    Toast.makeText(
                        this@DeleteUserActivity,
                        "User Deleted",
                        Toast.LENGTH_SHORT
                    ).show()

                    loadUsers()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}