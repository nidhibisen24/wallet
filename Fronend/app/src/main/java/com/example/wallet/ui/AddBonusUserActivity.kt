package com.example.wallet.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.wallet.R
import com.example.wallet.adapter.AddBonusUserAdapter
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.launch

class AddBonusUserActivity : AppCompatActivity() {

    private lateinit var rvUsers: RecyclerView
    private lateinit var btnBack: CardView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_bonus_user)
        val adminId = intent.getIntExtra("USER_ID", 0)

        rvUsers = findViewById(R.id.rvUsers)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        btnBack = findViewById(R.id.btnBack)

        rvUsers.layoutManager =
            LinearLayoutManager(this)



        btnBack.setOnClickListener {
            finish()
        }

        swipeRefresh.setOnRefreshListener {
            loadUsers(adminId)
        }

        loadUsers(adminId)


    }

    private fun loadUsers(adminId: Int) {
        swipeRefresh.isRefreshing = true


        lifecycleScope.launch {

            try {

                val users =
                    RetrofitClient.api.getAllUsers(adminId)

                rvUsers.adapter =
                    AddBonusUserAdapter(users) { userId ->

                        val intent = Intent(
                            this@AddBonusUserActivity,
                            AddBonusActivity::class.java
                        )

                        intent.putExtra(
                            "USER_ID",
                            userId
                        )

                        startActivity(intent)
                    }

            } catch (e: Exception) {
                e.printStackTrace()
            }finally {

                swipeRefresh.isRefreshing = false

            }
        }
    }
}