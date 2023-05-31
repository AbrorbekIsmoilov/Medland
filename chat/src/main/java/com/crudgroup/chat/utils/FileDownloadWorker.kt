package com.crudgroup.chat.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.crudgroup.chat.utils.Constants.CHANNEL_DESCRIPTION
import com.crudgroup.chat.utils.Constants.CHANNEL_ID
import com.crudgroup.chat.utils.Constants.CHANNEL_NAME
import com.crudgroup.chat.utils.Constants.KEY_FILE_NAME
import com.crudgroup.chat.utils.Constants.KEY_FILE_TYPE
import com.crudgroup.chat.utils.Constants.KEY_FILE_URI
import com.crudgroup.chat.utils.Constants.KEY_FILE_URL
import com.crudgroup.chat.utils.Constants.NOTIFICATION_ID
import com.example.chat.R
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class FileDownloadWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext = context, params = workerParameters) {

    override suspend fun doWork(): Result {
        val fileUrl = inputData.getString(KEY_FILE_URL) ?: ""
        val fileName = inputData.getString(KEY_FILE_NAME) ?: ""
        val fileTypeNumber = inputData.getInt(KEY_FILE_TYPE, 0)
        var fileType = ""

        when (fileTypeNumber) {
            2 -> fileType = "PNG"
            3 -> fileType = "PDF"
            4 -> fileType = "MP3"
        }

        Log.e("TAG", "doWork: $fileUrl | $fileName | $fileType")

        if (fileName.isEmpty()
            || fileType.isEmpty()
            || fileUrl.isEmpty()
        ) {
            Result.failure()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = CHANNEL_NAME
            val description = CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)

        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_download)
            .setContentTitle("Downloading your file...")
            .setOngoing(true)
            .setProgress(0, 0, true)

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())

        val uri = getSavedFileUri(
            fileName = fileName,
            fileType = fileType,
            fileUrl = fileUrl,
            context = context
        )

        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
        return if (uri != null) {
            Result.success(workDataOf(KEY_FILE_URI to uri.toString()))
        } else {
            Result.failure()
        }
    }

    private fun getSavedFileUri(
        fileName: String,
        fileType: String,
        fileUrl: String,
        context: Context
    ): Uri? {
        val mimeType = when (fileType) {
            "PDF" -> "application/pdf"
            "PNG" -> "image/png"
            "MP3" -> "audio/mp3"
            else -> ""
        }

        if (mimeType.isEmpty()) return null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Download")
            }

            val resolver = context.contentResolver

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            return if (uri != null) {
                URL(fileUrl).openStream().use { input ->
                    resolver.openOutputStream(uri).use { output ->
                        input.copyTo(output!!, DEFAULT_BUFFER_SIZE)
                    }
                }
                uri
            } else {
                null
            }

        } else {

            val target = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                fileName
            )
            URL(fileUrl).openStream().use { input ->
                FileOutputStream(target).use { output ->
                    input.copyTo(output)
                }
            }

            return target.toUri()
        }
    }
}