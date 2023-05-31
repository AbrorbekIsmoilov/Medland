package uz.crud.data.model

import java.io.Serializable

data class UserDataInfo(
    val doctorSurname: String?,
    val doctorName: String?,
    val doctorImage: String?,
    val onlineTime: String?,
    val doctorId: String?,
    val userId: String?,
    val userName: String?,
    val userImage: String?,
    val token: String?,
    val categoryId: Int?,
    val type: String?
) : Serializable
