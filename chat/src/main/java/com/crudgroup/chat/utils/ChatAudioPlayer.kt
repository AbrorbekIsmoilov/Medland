package com.crudgroup.chat.utils

import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.IOException

object ChatAudioPlayer {

    private val mediaPlayer = MediaPlayer()

    var audiBehavior: AudiBehavior? = null

    fun playAudio(
        context: Context,
        name: String,
        uri: String,
        audiBehavior: AudiBehavior,
        callback: (duration: Int) -> Unit,
        audioListener: (mediaPlayer: MediaPlayer?) -> Unit
    ): MediaPlayer {
        this.audiBehavior?.clearResources()
        this.audiBehavior = audiBehavior


        val uriPath = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            getRealPathFromURI(context, Uri.parse(uri))
        } else {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                name
            ).path
        }

        try {
            mediaPlayer.apply {
                reset()
                setDataSource(uriPath)
                prepare()
                start()
                setOnCompletionListener(audioListener)
                callback.invoke(duration)
            }

        } catch (e: IOException) {
            Log.e("TAG", "playAudio: ", e)
        }

        return mediaPlayer
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(
                contentUri,
                proj,
                null,
                null,
                null
            )
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(proj[0])

            cursor.getString(columnIndex)
        } finally {
            cursor?.close()
        }
    }
}