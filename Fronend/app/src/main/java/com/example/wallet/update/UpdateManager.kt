package com.example.wallet.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.wallet.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class UpdateManager(
    private val context: Context,
    private val api: ApiService
) {

    suspend fun downloadAndInstallApk(): Result<Unit> {

        return try {

            val responseBody = api.downloadLatestApk()

            val apkFile = withContext(Dispatchers.IO) {

                val downloadsDir =
                    context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

                val file = File(
                    downloadsDir,
                    "app-update.apk"
                )

                responseBody.byteStream().use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                file
            }

            installApk(apkFile)

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    private fun installApk(file: File) {

        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {

            setDataAndType(
                uri,
                "application/vnd.android.package-archive"
            )

            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }
}