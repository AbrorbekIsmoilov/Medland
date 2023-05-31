package uz.crud.data.utils

import java.time.LocalDate

object Constants {
    const val MESSAGE_TYPE = 1
    const val IMAGE_TYPE = 2
    const val PDF_TYPE = 3
    const val AUDIO_TYPE = 4
    const val VIDEO_CHAT_TYPE = 5
    const val NOTHING_TYPE = 10

    const val SHARED_NAME = "firebase_shared"
    const val AUTOSTART_DATA_KEY = "autostart_data_key"

    const val PDF_URL = "https://projectmedland.uz/images/"

    fun getCurrentDate(): String {
        val current = LocalDate.now()
        return current.toString()
    }
}