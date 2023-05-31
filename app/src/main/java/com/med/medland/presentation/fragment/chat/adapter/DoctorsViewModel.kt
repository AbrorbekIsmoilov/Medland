package com.med.medland.presentation.fragment.chat.adapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.med.medland.domain.model.Doctor
import com.med.medland.domain.repository.ChatRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DoctorsViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    val onlineDoctors: SharedFlow<List<Doctor>> = flow {
        emitAll(repository.getOnlineDoctors())
    }.shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)

    val recentMessagedDoctors: SharedFlow<List<Doctor>> = flow {
        emitAll(repository.getRecentMessagedDoctors())
    }.shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)

    class Factory @Inject constructor(
        private val repository: ChatRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DoctorsViewModel(repository) as T
        }
    }
}