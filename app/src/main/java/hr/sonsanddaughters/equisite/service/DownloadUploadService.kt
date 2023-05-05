package hr.sonsanddaughters.equisite.service

import android.app.Service
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import hr.sonsanddaughters.equisite.R
import hr.sonsanddaughters.equisite.util.FirebaseUtil
import hr.sonsanddaughters.equisite.util.NotificationUtil
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


class DownloadUploadService : Service() {

    private lateinit var notificationChannel: NotificationUtil

    override fun onCreate() {
        super.onCreate()
        notificationChannel = NotificationUtil(applicationContext)
        notificationChannel.createNotificationChannel("download_channel_id", "Download channel")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val userId = intent?.getStringExtra("userId")
        val query = FirebaseUtil.db.collection("users").whereEqualTo("uid", userId)

        query.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    val document = snapshot.documents.firstOrNull()
                    if (document!!.exists()) {
                        saveDocument(document)
                    }
                    else {
                        notificationChannel.showNotification(
                            "download_channel_id",
                            2,
                            getString(R.string.error),
                            getString(R.string.there_was_an_error_with_your_download)
                        )
                    }
                }
                else {
                    notificationChannel.showNotification(
                        "download_channel_id",
                        3,
                        getString(R.string.error),
                        getString(R.string.there_was_an_error_trying_to_access_your_file)
                    )
                }
                stopSelf()
            }
        return START_NOT_STICKY
    }

    private fun saveDocument(document: DocumentSnapshot) {
        val fileName = "MyInformation.json"
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(directory, fileName)

        try {
            val fileOutputStream = FileOutputStream(file)
            val dataMap = document.data
            val jsonData = Gson().toJson(dataMap)
            fileOutputStream.write(jsonData.toByteArray())
            fileOutputStream.close()

            notificationChannel.showNotification(
                "download_channel_id",
                1,
                getString(R.string.success),
                getString(R.string.your_file_was_successfully_downloaded)
            )
        }
        catch (e: Exception) {
            notificationChannel.showNotification(
                "download_channel_id",
                4,
                getString(R.string.error),
                e.message.toString()
            )
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}