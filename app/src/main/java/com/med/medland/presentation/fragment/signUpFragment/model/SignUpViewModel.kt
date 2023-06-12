package com.med.medland.presentation.fragment.signUpFragment.model

import android.os.CountDownTimer
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.med.medland.data.api.retrofit.ApiResult
import com.med.medland.presentation.fragment.otherComponents.model.DetailResponseModel
import com.med.medland.presentation.fragment.otherComponents.model.GetRegionModel
import com.med.medland.presentation.fragment.signUpFragment.repository.SignUpRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel : ViewModel() {
    private var signUpRepository = SignUpRepository()

    val sendPhoneNumberResponse = MutableLiveData<ApiResult<DetailResponseModel>>()
    val sendNumberAndEmailResponse = MutableLiveData<ApiResult<DetailResponseModel>>()
    val sendSmsCodeResponse = MutableLiveData<ApiResult<DetailResponseModel>>()
    val sendSmsRequest = MutableLiveData(false)
    val replaySendSmsRequest = MutableLiveData(true)
    val smsCodeHasBeenWrite = MutableLiveData(false)
    val getRegionsLiveData = MutableLiveData<ApiResult<ArrayList<GetRegionModel>>>()
    val createPatientLiveData = MutableLiveData<ApiResult<DetailResponseModel>>()

    fun countDownTimer(timerTv : TextView) {
        object : CountDownTimer(59000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished <10000) timerTv.text = "0 : 0${millisUntilFinished / 1000}"
                else timerTv.text = "0 : ${millisUntilFinished / 1000}"
            }
            override fun onFinish() {
                replaySendSmsRequest.postValue(false)
            }
        }.start()
    }

    fun getRegions() {
        viewModelScope.launch {
            try {
                val response = signUpRepository.getRegions()
                if (response.isSuccessful) getRegionsLiveData.postValue(ApiResult.success(response.body(),response))
                else getRegionsLiveData.postValue(ApiResult.error(HttpException(response)))
            }
            catch (t : Throwable) {
                getRegionsLiveData.postValue(ApiResult.error(t))
            }
        }
    }

    fun sendPhoneNumber(phoneNumber : String, status : String){
        viewModelScope.launch {
            try {
                val response = signUpRepository.sendPhoneNumber(phoneNumber, status)
                if (response.isSuccessful) sendPhoneNumberResponse.postValue(ApiResult.success(response.body(), response))
                else sendPhoneNumberResponse.postValue(ApiResult.error(HttpException(response)))

            }
            catch (t: Throwable){ sendPhoneNumberResponse.postValue(ApiResult.error(t)) }
        }
    }

    fun sendNumberAndEmail(phoneNumber : String, email : String){
        viewModelScope.launch {
            try {
                val response = signUpRepository.sendPhoneNumber(phoneNumber, email)
                if (response.isSuccessful) sendPhoneNumberResponse.postValue(ApiResult.success(response.body(), response))
                else sendPhoneNumberResponse.postValue(ApiResult.error(HttpException(response)))

            }
            catch (t: Throwable){ sendPhoneNumberResponse.postValue(ApiResult.error(t)) }
        }
    }

    fun sendSmsCode(phoneNumber : String, code : String){
        viewModelScope.launch {
            try {
                val response = signUpRepository.sendSmsCode(phoneNumber, code)
                if (response.isSuccessful) sendSmsCodeResponse.postValue(ApiResult.success(response.body(), response))
                else sendSmsCodeResponse.postValue(ApiResult.error(HttpException(response)))

            }
            catch (t: Throwable){ sendSmsCodeResponse.postValue(ApiResult.error(t)) }
        }
    }

    fun createPatientModel(patientCreateModel: PatientCreateModel) {
        viewModelScope.launch {
            try {
                val response = signUpRepository.cretePatient(patientCreateModel)
                if (response.isSuccessful) createPatientLiveData.postValue(ApiResult.success(response.body(), response))
                else createPatientLiveData.postValue(ApiResult.error(HttpException(response)))
            }
            catch (t : Throwable) {
                createPatientLiveData.postValue(ApiResult.error(t))
            }
        }
    }
}