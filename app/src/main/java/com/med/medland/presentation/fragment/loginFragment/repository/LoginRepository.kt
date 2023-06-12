package com.med.medland.presentation.fragment.loginFragment.repository

import com.med.medland.data.api.apiRequest.SignInAndSignUpApi
import com.med.medland.data.api.retrofitCreate.RetrofitInstance
import com.med.medland.presentation.fragment.loginFragment.model.LoginResponseModel
import retrofit2.Response

class LoginRepository {

    private val api: SignInAndSignUpApi by lazy { RetrofitInstance.api }

    suspend fun getToken(username: String, password: String): Response<LoginResponseModel> {
        return api.getToken(username, password)
    }

}