package com.example.wallet.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
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
    private lateinit var etSearchUsers: EditText

    private var adminId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_delete_user)

        adminId =
            intent.getIntExtra(
                "USER_ID",
                0
            )

        // Views
        rvUsers =
            findViewById(R.id.rvUsers)

        btnBack =
            findViewById(R.id.btnBack)

        etSearchUsers =
            findViewById(R.id.etSearchUsers)

        // RecyclerView
        rvUsers.layoutManager =
            LinearLayoutManager(this)

        // Load all users initially
        loadUsers()

        // Search
        etSearchUsers.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    val search =
                        s?.toString()
                            ?.trim()
                            ?: ""

                    if (search.isEmpty()) {

                        loadUsers()

                    } else {

                        searchUsers(search)

                    }
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        // Back button
        btnBack.setOnClickListener {

            finish()

        }
    }

    /**
     * Load all users
     */
    private fun loadUsers() {

        lifecycleScope.launch {

            try {

                val users =
                    RetrofitClient.api
                        .getAllUsers(adminId)

                rvUsers.adapter =
                    DeleteUserAdapter(
                        users
                    ) { userId ->

                        showDeleteDialog(
                            userId
                        )
                    }

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    this@DeleteUserActivity,
                    "Failed to load users",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Search users by name or phone
     */
    private fun searchUsers(
        search: String
    ) {

        lifecycleScope.launch {

            try {

                val users =
                    RetrofitClient.api
                        .searchUser(
                            adminId,
                            search
                        )

                rvUsers.adapter =
                    DeleteUserAdapter(
                        users
                    ) { userId ->

                        showDeleteDialog(
                            userId
                        )
                    }

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    this@DeleteUserActivity,
                    "Search failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Delete confirmation
     */
    private fun showDeleteDialog(
        userId: Int
    ) {

        AlertDialog.Builder(this)
            .setTitle("Delete User")
            .setMessage(
                "Are you sure you want to delete this user?"
            )
            .setPositiveButton(
                "Delete"
            ) { _, _ ->

                deleteUser(userId)

            }
            .setNegativeButton(
                "Cancel",
                null
            )
            .show()
    }

    /**
     * Delete user
     */
    private fun deleteUser(
        userId: Int
    ) {

        lifecycleScope.launch {

            try {

                val response =
                    RetrofitClient.api
                        .deleteUser(userId)

                if (response.isSuccessful) {

                    Toast.makeText(
                        this@DeleteUserActivity,
                        "User Deleted",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Keep the current search
                    val currentSearch =
                        etSearchUsers.text
                            .toString()
                            .trim()

                    if (currentSearch.isEmpty()) {

                        loadUsers()

                    } else {

                        searchUsers(
                            currentSearch
                        )
                    }

                } else {

                    Toast.makeText(
                        this@DeleteUserActivity,
                        "Failed to delete user",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    this@DeleteUserActivity,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}