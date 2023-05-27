package com.crudgroup.chat.utils

import uz.crud.data.model.ChatData

sealed class FileDownloadState(
    val data: ChatData? = null,
    val id: String? = null
) {
    class Success<T>(data: ChatData?) : FileDownloadState(data)
    class Running<T>(id: String?) : FileDownloadState(id = id)
    class Failed<T>(id: String?) : FileDownloadState(id = id)
}
