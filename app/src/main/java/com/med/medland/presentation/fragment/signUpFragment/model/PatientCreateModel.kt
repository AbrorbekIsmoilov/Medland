package com.med.medland.presentation.fragment.signUpFragment.model

data class PatientCreateModel(

    val ism : String,
    val tugilgan_sana: String,
    val jinsi: String,
    val manzil: String,
    val mamlakat: String,
    val viloyat_id: Int,
    val tuman_id: Int,
    val tel: String,
    val gmail: String,
    val password: String,
    val disabled: Boolean
)
