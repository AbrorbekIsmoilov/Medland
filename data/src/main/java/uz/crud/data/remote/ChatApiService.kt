package uz.crud.data.remote

import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import uz.crud.data.model.Chat
import uz.crud.data.model.ChatResponseBody
import uz.crud.data.model.FcmTokenData
import uz.crud.data.model.Question

interface ChatApiService {

    @Multipart
    @POST("xamshira_chat")
    suspend fun postMessage(
        @Part("user_doctor") user_doctor: RequestBody,
        @Part("chat") chat: RequestBody,
        @Part("shifokor_id") shifokor_id: RequestBody,
        @Part("foydalanuvchi_id") foydalanuvchi_id: RequestBody,
        @Part file: MultipartBody.Part? = null,
    ): ChatResponseBody

    @GET("xamshira/chat/mobile/{shifokor_id}/{client_id}")
    suspend fun getMessage(
        @Path("shifokor_id") doctorId: String?,
        @Path("client_id") clientId: String?
    ): List<Chat>

    @PUT("bemor_chat_update")
    suspend fun updateChat(
        @Query("text") text: String?,
        @Query("chat_id") chatId: String?
    )

    @GET("bemor_videochat_get")
    suspend fun getFcmToken(): Response<FcmTokenData>

    @POST("bemor_videochat_token_fcm")
    suspend fun senNotification(
        @Query("to") to: String?,
        @Query("body") body: String = "Calling",
        @Query("title") title: String?,
        @Query("romid") roomId: String?,
        @Query("image") image: String?,
        @Query("name") name: String?
    ): String

    suspend fun getChatStatus()

    fun getQuestions(): Flow<List<Question>>
}