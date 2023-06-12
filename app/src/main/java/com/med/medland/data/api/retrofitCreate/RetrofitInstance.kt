package com.med.medland.data.api.retrofitCreate

import com.med.medland.data.api.apiRequest.SignInAndSignUpApi
import com.med.medland.data.locale.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val client = OkHttpClient.Builder().apply { addInterceptor(ApiInterceptor()) }.build()

    val api: SignInAndSignUpApi by lazy { retrofit.create(SignInAndSignUpApi :: class.java) }

    val retrofit: Retrofit by lazy {
         Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}