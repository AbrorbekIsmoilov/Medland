package com.med.medland.data.room.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MyInfoTable")
data class MyInfoTable(
    @PrimaryKey(autoGenerate = false)
    val myId : Int,
    val date_of_brith : String?,
    val address : String,
    val province_id : Int,
    val phone : Long,
    val card_number : String,
    val password : String,
    val disabled : Boolean,
    val block: Boolean,
    val name : String,
    val gender: String,
    val id : String,
    val country : String,
    val district_id : Int,
    val balance : Double,
    val username : String,
    val online : String

)
