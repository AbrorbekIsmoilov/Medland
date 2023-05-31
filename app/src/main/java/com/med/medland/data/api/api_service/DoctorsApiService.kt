package com.med.medland.data.api.api_service

import com.med.medland.data.dto.DoctorDto
import com.med.medland.domain.model.Doctor
import retrofit2.http.GET

interface DoctorsApiService {

    @GET("")
    suspend fun getOnlineDoctors(): List<Doctor>

    @GET("bemor_chat_mobile_all")
    suspend fun getRecentMessagedDoctors(): List<DoctorDto>
}