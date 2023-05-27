package com.med.medland.presentation.fragment.loginFragment.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.med.medland.data.api.retrofit.MedlandApi
import com.med.medland.data.api.retrofit.RetrofitInstance
import com.med.medland.data.room.dao.MyInfoDao
import com.med.medland.data.room.database.UserDataBase
import com.med.medland.data.room.table.MyInfoTable
import com.med.medland.presentation.fragment.loginFragment.model.LoginResponseModel
import retrofit2.Response

class LoginRepository {

    private val api: MedlandApi by lazy { RetrofitInstance.api }

    suspend fun getToken(username: String, password: String): Response<LoginResponseModel> {
        return api.getToken(username, password)
    }

}