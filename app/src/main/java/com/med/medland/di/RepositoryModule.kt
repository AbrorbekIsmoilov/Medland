package com.med.medland.di

import com.med.medland.data.repository.ChatRepositoryImpl
import com.med.medland.domain.repository.ChatRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    fun bindChatRepository(chatRepository: ChatRepositoryImpl): ChatRepository
}