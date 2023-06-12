package com.med.medland.presentation.fragment.pincodeFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PinCodeViewModel : ViewModel() {

    val pinCodeText = MutableLiveData("")
    val savePinCode = MutableLiveData("")

    fun addCode(code : String) : Boolean {

        return if (pinCodeText.value?.length!! < 4) {
            pinCodeText.postValue(pinCodeText.value + code)
            true
        } else false
    }
    fun removeCode() : Boolean {
        return if (pinCodeText.value!!.isNotEmpty()) {
            val newValue = StringBuffer(pinCodeText.value!!).deleteCharAt(pinCodeText.value!!.length - 1)
            pinCodeText.value = newValue.toString()
            true
        } else false
    }
}