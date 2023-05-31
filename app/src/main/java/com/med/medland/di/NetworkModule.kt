package com.med.medland.di

import com.med.medland.data.api.api_service.DoctorsApiService
import com.med.medland.data.api.retrofit.RetrofitInstance
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import uz.crud.data.remote.ChatApiService

@Module
class NetworkModule {

    @[AppScope Provides]
    fun provideChatApiService(retrofitInstance: Retrofit): ChatApiService {
        return retrofitInstance.create(ChatApiService::class.java)
    }

    @[AppScope Provides]
    fun provideRetrofit(): Retrofit = RetrofitInstance.retrofit

    @[AppScope Provides]
    fun provideDoctorsApiService(retrofitInstance: Retrofit): DoctorsApiService {
        return retrofitInstance.create(DoctorsApiService::class.java)
    }
}