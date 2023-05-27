package com.med.medland.di

import com.med.medland.data.api.retrofit.RetrofitInstance
import dagger.Module
import dagger.Provides
import uz.crud.data.remote.ChatApiService

@Module
class NetworkModule {

    @[AppScope Provides]
    fun provideChatApiService(retrofitInstance: RetrofitInstance): ChatApiService {
        return retrofitInstance.retrofit.create(ChatApiService::class.java)
    }

    @[AppScope Provides]
    fun provideRetrofit(): RetrofitInstance = RetrofitInstance
}