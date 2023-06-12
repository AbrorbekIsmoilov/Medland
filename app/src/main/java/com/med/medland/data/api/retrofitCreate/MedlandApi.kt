package com.med.medland.data.api.retrofitCreate

import com.med.medland.presentation.fragment.loginFragment.model.LoginResponseModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MedlandApi {

    @FormUrlEncoded
    @POST("bemor_login")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponseModel>

}