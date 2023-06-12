package com.med.medland.presentation.fragment.otherComponents.model

data class GetRegionModel(
    val nomi : String,
    val id : Int,
    val rasm : String,
    val tuman_viloyat : ArrayList<GetDistrictModel>
)
