package com.med.medland.data.repository

import android.util.Log
import com.med.medland.data.api.api_service.DoctorsApiService
import com.med.medland.data.dto.DoctorDto
import com.med.medland.domain.model.Doctor
import com.med.medland.domain.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val apiService: DoctorsApiService
) : ChatRepository {

    override suspend fun getOnlineDoctors(): Flow<List<Doctor>> = withContext(Dispatchers.IO) {
        val result: List<Doctor> = try {
//            apiService.getOnlineDoctors()
            emptyList()
        } catch (t: Throwable) {
            Log.e(TAG, "getOnlineDoctors: ${t.message}", t)
            emptyList()
        }
        flowOf(result)
    }

    override suspend fun getRecentMessagedDoctors(): Flow<List<Doctor>> =
        withContext(Dispatchers.IO) {
            val result: List<Doctor> = try {
                val list = arrayListOf<Doctor>()
                apiService.getRecentMessagedDoctors().forEach { doctorDto: DoctorDto ->
                    doctorDto.shifokor_yozgan?.forEach { doctor: Doctor ->
                        list.add(doctor)
                    }
                }
                list
            } catch (t: Throwable) {
                Log.e(TAG, "getRecentMessagedDoctors: ${t.message}", t)
                emptyList()
            }
            flowOf(result)
        }

    companion object {
        const val TAG = "ChatRepositoryImpl"
    }
}