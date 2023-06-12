package com.med.medland.presentation.fragment.splashFragment.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.med.medland.data.api.retrofit.ApiResult
import com.med.medland.presentation.fragment.loginFragment.model.LoginResponseModel
import com.med.medland.presentation.fragment.splashFragment.repository.SplashRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SplashViewModel : ViewModel() {

    private var loginRepository  = SplashRepository()

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