package com.med.medland.data.api.retrofitCreate

import com.orhanobut.hawk.Hawk
import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor: Interceptor {

    private fun getToken(): String? { return Hawk.get("token", null) }


    override fun intercept(chain: Interceptor.Chain): Response {
        val token = getToken()

        val request = chain.request().newBuilder().apply {
                if (token != null) {
                    addHeader("Authorization", "Bearer $token")
                }
            }.build()

        return chain.proceed(request)
    }

}