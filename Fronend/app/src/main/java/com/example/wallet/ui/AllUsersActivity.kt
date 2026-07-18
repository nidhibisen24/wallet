package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R
import com.example.wallet.adapter.UserAdapter
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
class AllUsersActivity : AppCompatActivity() {

    private lateinit var rvUsers: RecyclerView
    private lateinit var btnBack: CardView
    private lateinit var etSearchUsers: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_all_users)

        val userId = intent.getIntExtra("USER_ID", 0)
        etSearchUsers = findViewById(R.id.etSearchUsers)
        rvUsers = findViewById(R.id.rvUsers)
        rvUsers.layoutManager = LinearLayoutManager(this)

        loadUsers(userId)
        etSearchUsers.addTextChangedListener(object : TextWatcher {

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

                val search = s.toString().trim()

                if (search.isEmpty()) {

                    loadUsers(userId)

                } else {

                    searchUsers(userId, search)

                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
    }
    private fun searchUsers(
        userId: Int,
        search: String
    ) {

        lifecycleScope.launch {

            try {

                val users = RetrofitClient.api.searchUser(
                    userId,
                    search
                )

                rvUsers.adapter =
                    UserAdapter(users) { selectedUserId ->

                        val intent = Intent(
                            this@AllUsersActivity,
                            UserDetailsActivity::class.java
                        )

                        intent.putExtra(
                            "USER_ID",
                            selectedUserId
                        )

                        startActivity(intent)
                    }

            } catch (e: Exception) {

                e.printStackTrace()

            }
        }
    }
    private fun loadUsers(userId: Int) {

        lifecycleScope.launch {

            try {

                val users = RetrofitClient.api.getAllUsers(userId)

                rvUsers.adapter = UserAdapter(users) { selectedUserId ->

                    val intent = Intent(
                        this@AllUsersActivity,
                        UserDetailsActivity::class.java
                    )

                    intent.putExtra("USER_ID", selectedUserId)

                    startActivity(intent)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}
