package com.med.medland.presentation.fragment.loginFragment.model

import com.med.medland.presentation.fragment.profileFragment.model.MyInfoModel

data class LoginResponseModel(
    val access_token: String,
    val user : MyInfoModel
)
