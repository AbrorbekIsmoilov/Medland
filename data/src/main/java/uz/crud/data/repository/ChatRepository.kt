package uz.crud.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import uz.crud.data.local.ChatDao
import uz.crud.data.model.*
import uz.crud.data.remote.ChatApiService
import uz.crud.data.utils.NetworkUtils
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val apiService: ChatApiService,
    private val dao: ChatDao,
    private val context: Context,
    private val sPref: SharedPreferences
) {

    suspend fun getChats(
        doctorId: String?,
        userId: String?
    ): List<ChatData> = withContext(Dispatchers.IO) {
        if (NetworkUtils.isConnect(context)) {
            val list = arrayListOf<ChatData>()
            var chatData: ChatData
            apiService.getMessage(
                doctorId,
                userId
            ).forEach { item: Chat ->
                chatData = item.toChatData()
                chatData.downloadedUri = dao.findDownloadUriById(chatData.id)
                list.add(chatData)
            }
            dao.deleteAllChats()
            dao.insertAllChat(list)
            list
        } else {
            dao.getAllChats()
        }
    }

    suspend fun postMessage(
        userId: String?,
        doctorId: String?,
        message: String?,
        file: File?,
        mediaType: String?
    ): ChatResponseBody = withContext(Dispatchers.IO) {
        val a = apiService.postMessage(
            getRequestBodyFromString("1"),
            getRequestBodyFromString(message),
            getRequestBodyFromString(doctorId),
            getRequestBodyFromString(userId),
            getPartFromFile(file, mediaType)
        )
        a
    }

    suspend fun getFcmToken(): String = try {
        val response: Response<FcmTokenData> = apiService.getFcmToken()
        val data: FcmTokenData? = response.body()
        sPref.edit()
            .putInt("ip", data?.ip ?: 0)
            .apply()
        data?.token ?: ""
    } catch (e: IOException) {
        Log.e("ChatRepository", "getFcmToken: ${e.message}", e.cause)
        "User not found"
    }

    suspend fun sendNotificationToUser(
        to: String,
        title: String,
        roomId: String,
        image: String?
    ): Boolean {
        val response = apiService.senNotification(
            to = to,
            title = title,
            roomId = roomId,
            image = image,
            name = title
        )
        Log.e("ChatRepository", "sendNotificationToUser: $response")
        return false
    }

    suspend fun getChatStatus(doctorId: String?): Boolean = withContext(Dispatchers.IO) {

        true
    }

    fun getQuestions(): Flow<List<Question>> {
        return apiService.getQuestions()
    }

    suspend fun updateLocalChat(data: ChatData?) = withContext(Dispatchers.IO) {
        if (data != null) {
            dao.update(data.downloadedUri.toString(), data.id.toString())
        }
    }

    private fun getRequestBodyFromString(string: String?): RequestBody {
        return (string ?: "").toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun getPartFromFile(file: File?, mediaType: String?): MultipartBody.Part? {
        return if (file == null) {
            null
        } else {
            val requestBody: RequestBody = file.asRequestBody(mediaType?.toMediaTypeOrNull())
            MultipartBody.Part.createFormData("file", file.name, requestBody)
        }
    }

    suspend fun updateChat(message: String, chatId: String?) = withContext(Dispatchers.IO) {
        try {
            apiService.updateChat(message, chatId)
        } catch (_: Exception) {
        }
    }
}