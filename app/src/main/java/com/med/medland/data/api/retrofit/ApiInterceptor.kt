package com.med.medland.data.api.retrofit

import android.util.Log
import com.med.medland.data.locale.Constants
import com.orhanobut.hawk.Hawk
import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor: Interceptor {

    private fun getToken(): String? { return Hawk.get(Constants.TOKEN, null) }


    override fun intercept(chain: Interceptor.Chain): Response {
        val token = getToken()

        Log.e("TAG", "intercept: $token", )

        val request = chain.request().newBuilder().apply {
                if (token != null) {
                    addHeader("Authorization", "Bearer $token")
                }
            }.build()

        return chain.proceed(request)
    }

}