package com.med.medland.presentation.fragment.signUpFragment.repository

import com.med.medland.data.api.apiRequest.SignInAndSignUpApi
import com.med.medland.data.api.retrofitCreate.RetrofitInstance
import com.med.medland.presentation.fragment.otherComponents.model.DetailResponseModel
import com.med.medland.presentation.fragment.otherComponents.model.GetRegionModel
import com.med.medland.presentation.fragment.signUpFragment.model.PatientCreateModel
import retrofit2.Response

class SignUpRepository {

    private val api: SignInAndSignUpApi by lazy { RetrofitInstance.api }

    suspend fun sendPhoneNumber(phoneNumber : String, status: String): Response<DetailResponseModel> {
        return api.sendPhoneNumber(phoneNumber, status)
    }

    suspend fun sendNumberAndEmail(phoneNumber : String, email: String): Response<DetailResponseModel> {
        return api.sendNumberAndGmail(phoneNumber, email)
    }

    suspend fun sendSmsCode(phoneNumber : String, code: String): Response<DetailResponseModel> {
        return api.smsStatusValidation(phoneNumber, code)
    }

    suspend fun getRegions(): Response<ArrayList<GetRegionModel>> {
        return api.getRegions()
    }

    suspend fun cretePatient(patientCreateModel: PatientCreateModel): Response<DetailResponseModel> {
        return api.createPatient(patientCreateModel)
    }
}