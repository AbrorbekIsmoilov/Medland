package com.med.medland.presentation.fragment.loginFragment.model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.med.medland.data.api.retrofit.ApiResult
import com.med.medland.data.room.database.UserDataBase
import com.med.medland.data.room.table.MyInfoTable
import com.med.medland.presentation.fragment.loginFragment.repository.LoginRepository
import com.med.medland.presentation.fragment.profileFragment.model.MyInfoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.HttpException

class LoginViewModel : ViewModel() {

    private var loginRepository = LoginRepository()

    val getTokenData = MutableLiveData<ApiResult<LoginResponseModel>>()

    fun getToken(userName: String, password: String){
        viewModelScope.launch {
            try {
                val response = loginRepository.getToken(userName, password)
                if (response.isSuccessful) getTokenData.postValue(ApiResult.success(response.body(), response))
                else getTokenData.postValue(ApiResult.error(HttpException(response)))

            }
            catch (t: Throwable){ getTokenData.postValue(ApiResult.error(t)) }
        }
    }
}