package uz.crud.data.model

import uz.crud.data.utils.Constants.AUDIO_TYPE
import uz.crud.data.utils.Constants.IMAGE_TYPE
import uz.crud.data.utils.Constants.MESSAGE_TYPE
import uz.crud.data.utils.Constants.NOTHING_TYPE
import uz.crud.data.utils.Constants.PDF_TYPE
import uz.crud.data.utils.Constants.PDF_URL
import uz.crud.data.utils.Constants.getCurrentDate

data class Chat(
    val id: String?,
    val user_doctor: Int,
    val shifokor_id: String?,
    val foydalanuvchi_id: String?,
    val chat: String?,
    val rasm: String?,
    val date: String?,
)

fun Chat.toChatData(): ChatData = ChatData(
    id = id,
    userDoctor = user_doctor,
    userId = foydalanuvchi_id,
    clientId = shifokor_id,
    name = if ((getType(rasm, chat) == MESSAGE_TYPE)) chat else rasm,
    type = getType(rasm, chat),
    url = PDF_URL + rasm,
    date = getDate(date)
)

private fun getType(url: String?, chat: String?): Int {
    if (url == null) {
        return MESSAGE_TYPE
    }

    if (url.length < 3) {
        return MESSAGE_TYPE
    }

    return when (url.substring(url.length - 3)) {
        "png", "jpeg" -> IMAGE_TYPE
        "pdf" -> PDF_TYPE
        "mp3" -> AUDIO_TYPE
        else -> NOTHING_TYPE
    }
}

private fun getDate(date: String?): String {
    if (date == null) {
        return ""
    }
    return if (date.substring(0, 10) == getCurrentDate()) {
        date.substring(11, date.length)
    } else {
        date
    }
}