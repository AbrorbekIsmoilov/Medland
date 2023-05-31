package com.crudgroup.chat.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.crudgroup.chat.multichoice.MultiChoiceHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.crud.data.model.ChatData
import uz.crud.data.model.ChatResponseBody
import uz.crud.data.model.Question
import uz.crud.data.repository.ChatRepository
import java.io.File
import javax.inject.Inject

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val multiChoiceHandler: MultiChoiceHandler
) : ViewModel() {

    private val _chatList = MutableSharedFlow<List<ChatData>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val chatList: SharedFlow<List<ChatData>> = _chatList.asSharedFlow()

    private val _result = MutableSharedFlow<ChatResponseBody>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val result: SharedFlow<ChatResponseBody> = _result.asSharedFlow()

    private val _videoChatToken = MutableSharedFlow<String>(
        replay = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1
    )
    val videoChatToken: SharedFlow<String> = _videoChatToken.asSharedFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    private val _questions = MutableSharedFlow<Question>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val questions: SharedFlow<Question> = _questions.asSharedFlow()

    private val _chatStatus = MutableStateFlow(true)
    val chatStatus: StateFlow<Boolean> = _chatStatus.asStateFlow()

//    init {
//        viewModelScope.launch {
//            multiChoiceHandler.setItemsFlow(viewModelScope, chatRepository.getQuestions())
//        }
//    }

    fun getMessage(
        doctorId: String?,
        userId: String?,
    ) {
        viewModelScope.launch {
            _chatList.tryEmit(
                chatRepository.getChats(
                    doctorId,
                    userId
                )
            )
        }
    }

    fun postMessage(
        doctorId: String?,
        userId: String?,
        message: String?,
        file: File?,
        mediaType: String?
    ) {
        viewModelScope.launch {
            _result.tryEmit(
                chatRepository.postMessage(
                    userId,
                    doctorId,
                    message,
                    file,
                    mediaType
                )
            )
        }
    }

    fun getFcmToken() {
        viewModelScope.launch {
            _loading.tryEmit(true)
            _videoChatToken.tryEmit(chatRepository.getFcmToken())
            _loading.tryEmit(false)
        }
    }

    fun updateLocalChat(data: ChatData?) {
        viewModelScope.launch {
            chatRepository.updateLocalChat(data)
        }
    }

    fun sendNotification(
        to: String,
        title: String,
        roomId: String,
        image: String?
    ) {
        viewModelScope.launch {
            try {
                _loading.tryEmit(
                    chatRepository.sendNotificationToUser(
                        to,
                        title,
                        roomId,
                        image
                    )
                )
            } catch (e: Exception) {
                Log.e("ChatViewModel", "sendNotification: ", e)
            }
        }
    }

    fun updateChat(message: String, chatId: String?) {
        viewModelScope.launch {
            chatRepository.updateChat(message, chatId)
        }
    }

    fun getChatStatus(doctorId: String?) {
        viewModelScope.launch {
            _chatStatus.emit(chatRepository.getChatStatus(doctorId))
        }
    }

    class Factory @Inject constructor(
        private val chatRepository: ChatRepository,
        private val multiChoiceHandler: MultiChoiceHandler
    ) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChatViewModel(chatRepository, multiChoiceHandler) as T
        }
    }
}