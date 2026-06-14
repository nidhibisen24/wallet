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

class AllUsersActivity : AppCompatActivity() {

    private lateinit var rvUsers: RecyclerView
    private lateinit var btnBack: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_all_users)

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
                    UserAdapter(users) { userId ->

                        val intent = Intent(
                            this@AllUsersActivity,
                            UserDetailsActivity::class.java
                        )

                        intent.putExtra(
                            "USER_ID",
                            userId
                        )

                        startActivity(intent)
                    }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}
