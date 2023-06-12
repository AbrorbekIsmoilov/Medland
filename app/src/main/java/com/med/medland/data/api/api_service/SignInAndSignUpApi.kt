package com.med.medland.data.api.api_service

import com.med.medland.presentation.fragment.loginFragment.model.LoginResponseModel
import com.med.medland.presentation.fragment.otherComponents.model.DetailResponseModel
import com.med.medland.presentation.fragment.otherComponents.model.GetRegionModel
import com.med.medland.presentation.fragment.signUpFragment.model.PatientCreateModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface SignInAndSignUpApi {

    @FormUrlEncoded
    @POST("bemor_login")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponseModel>


    @FormUrlEncoded
    @POST("send_sms")
    suspend fun sendPhoneNumber(
        @Field("phone_number") phoneNumber: String,
        @Field("status") status: String
    ) : Response<DetailResponseModel>

    @FormUrlEncoded
    @POST("send_google_accaunt_verifacation_code")
    suspend fun sendNumberAndGmail(
        @Field("phone_number") phoneNumber: String,
        @Field("account_name") accountName: String
    ) : Response<DetailResponseModel>

    @FormUrlEncoded
    @PUT("sms_status_validation")
    suspend fun smsStatusValidation(
        @Field("phone") phoneNumber: String,
        @Field("code") smsCode: String
    ) : Response<DetailResponseModel>

    @GET("bemor_hududlar")
    suspend fun getRegions() : Response<ArrayList<GetRegionModel>>


    @POST("foydalanuvchi/create")
    suspend fun createPatient(
        @Body createPatient : PatientCreateModel
    ) : Response<DetailResponseModel>

}