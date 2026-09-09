package com.example.wallet.ui

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.wallet.BuildConfig
import com.example.wallet.R
import com.example.wallet.data.AppUpdateResponse
import com.example.wallet.data.LoginRequest
import com.example.wallet.data.LoginResponse
import com.example.wallet.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class LoginActivity : AppCompatActivity() {

    private lateinit var etMobile: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
         * First show the login layout.
         * The update check happens before we decide
         * whether to open the dashboard.
         */
        setContentView(R.layout.activity_login)

        checkForUpdate {
            continueToApp()
        }
    }

    // =========================================================
    // CHECK LOGIN AND OPEN CORRECT SCREEN
    // =========================================================

    private fun continueToApp() {

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

                intent.putExtra(
                    "USER_ID",
                    sharedPref.getInt(
                        "user_id",
                        0
                    )
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

        } else {

            // User is not logged in.
            // Keep LoginActivity open.
            setupLoginScreen()
        }
    }

    // =========================================================
    // SETUP LOGIN SCREEN
    // =========================================================

    private fun setupLoginScreen() {

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

        val tvForgotPassword =
            findViewById<TextView>(
                R.id.tvForgotPassword
            )

        tvForgotPassword.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ForgotPasswordActivity::class.java
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

                                val intent =
                                    Intent(
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

    // =========================================================
    // APP UPDATE CHECK
    // =========================================================

    private fun checkForUpdate(
        onFinished: () -> Unit
    ) {

        lifecycleScope.launch {

            try {

                val update =
                    RetrofitClient.api.checkAppUpdate()

                val currentVersion =
                    BuildConfig.VERSION_CODE

                android.util.Log.d(
                    "APP_UPDATE",
                    "Current version: $currentVersion"
                )

                android.util.Log.d(
                    "APP_UPDATE",
                    "Server version: ${update.version_code}"
                )

                android.util.Log.d(
                    "APP_UPDATE",
                    "Update available: ${update.update_available}"
                )

                if (
                    update.update_available &&
                    update.version_code > currentVersion
                ) {

                    showUpdateDialog(
                        update,
                        onFinished
                    )

                } else {

                    // No update.
                    onFinished()
                }

            } catch (e: Exception) {

                android.util.Log.e(
                    "APP_UPDATE",
                    "Update check failed",
                    e
                )

                /*
                 * If the update server is unavailable,
                 * don't block the user from using the app.
                 */
                onFinished()
            }
        }
    }

    // =========================================================
    // UPDATE DIALOG
    // =========================================================

    private fun showUpdateDialog(
        update: AppUpdateResponse,
        onFinished: () -> Unit
    ) {

        if (
            isFinishing ||
            isDestroyed
        ) {
            return
        }

        val builder =
            AlertDialog.Builder(this)

        builder.setTitle(
            "Update Available"
        )

        builder.setMessage(
            "A new version (${update.version_name}) is available.\n\n" +
                    "Please update the app to get the latest features and improvements."
        )

        builder.setPositiveButton(
            "Update"
        ) { _, _ ->

            downloadApk(update)
        }

        if (!update.force_update) {

            builder.setNegativeButton(
                "Later"
            ) { dialog, _ ->

                dialog.dismiss()

                // Continue normally
                onFinished()
            }

        } else {

            /*
             * Force update:
             * user cannot close the dialog without
             * pressing Update.
             */
            builder.setCancelable(false)
        }

        builder.show()
    }

    // =========================================================
    // DOWNLOAD APK
    // =========================================================

    private fun downloadApk(
        update: AppUpdateResponse
    ) {

        val dialog =
            AlertDialog.Builder(this)
                .setTitle(
                    "Downloading Update"
                )
                .setMessage(
                    "Please wait..."
                )
                .setCancelable(false)
                .create()

        dialog.show()

        lifecycleScope.launch {

            try {

                val responseBody =
                    RetrofitClient.api.downloadLatestApk()

                val apkFile =
                    withContext(Dispatchers.IO) {

                        val downloadsDir =
                            getExternalFilesDir(
                                Environment.DIRECTORY_DOWNLOADS
                            )

                        if (
                            downloadsDir == null
                        ) {
                            throw Exception(
                                "Unable to access download folder"
                            )
                        }

                        val apkFile =
                            File(
                                downloadsDir,
                                "app-update.apk"
                            )

                        responseBody
                            .byteStream()
                            .use { input ->

                                apkFile
                                    .outputStream()
                                    .use { output ->

                                        input.copyTo(
                                            output
                                        )
                                    }
                            }

                        apkFile
                    }

                dialog.dismiss()

                android.util.Log.d(
                    "APP_UPDATE",
                    "APK downloaded: ${apkFile.absolutePath}"
                )

                installApk(apkFile)

            } catch (e: Exception) {

                dialog.dismiss()

                android.util.Log.e(
                    "APP_UPDATE",
                    "APK download failed",
                    e
                )

                Toast.makeText(
                    this@LoginActivity,
                    "Update download failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // =========================================================
    // OPEN ANDROID APK INSTALLER
    // =========================================================

    private fun installApk(
        apkFile: File
    ) {

        try {

            val apkUri: Uri =
                FileProvider.getUriForFile(
                    this,
                    "${BuildConfig.APPLICATION_ID}.provider",
                    apkFile
                )

            val intent =
                Intent(
                    Intent.ACTION_VIEW
                )

            intent.setDataAndType(
                apkUri,
                "application/vnd.android.package-archive"
            )

            intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )

            startActivity(intent)

        } catch (e: Exception) {

            android.util.Log.e(
                "APP_UPDATE",
                "Installation failed",
                e
            )

            Toast.makeText(
                this,
                "Unable to open APK installer",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}