package com.med.medland.presentation.fragment.splashFragment.repository

import com.med.medland.data.api.retrofit.MedlandApi
import com.med.medland.data.api.retrofit.RetrofitInstance
import com.med.medland.presentation.fragment.loginFragment.model.LoginResponseModel
import retrofit2.Response

class SplashRepository {

    private val api: MedlandApi by lazy { RetrofitInstance.api }

    suspend fun getToken(username: String, password: String): Response<LoginResponseModel> {
        return api.getToken(username, password)
    }
}