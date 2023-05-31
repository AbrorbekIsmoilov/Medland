package com.med.medland.data.dto

import com.google.gson.annotations.SerializedName
import com.med.medland.domain.model.Doctor

data class DoctorDto(
    val operator_yozgan: List<Operator>?,
    val shifokor_yozgan: List<Doctor>?
)

data class Operator(
    @SerializedName("Operator_chat")
    val operator_chat: String?,
    @SerializedName("operator_count")
    val operator_count: Int?
)