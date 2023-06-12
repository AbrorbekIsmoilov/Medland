package com.med.medland.presentation.fragment.loginFragment.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.med.medland.data.api.retrofitCreate.ApiResult
import com.med.medland.presentation.fragment.loginFragment.repository.LoginRepository
import kotlinx.coroutines.launch
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