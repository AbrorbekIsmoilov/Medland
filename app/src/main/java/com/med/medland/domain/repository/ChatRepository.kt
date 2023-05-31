package com.med.medland.domain.repository

import com.med.medland.domain.model.Doctor
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun getOnlineDoctors(): Flow<List<Doctor>>
    suspend fun getRecentMessagedDoctors(): Flow<List<Doctor>>
}