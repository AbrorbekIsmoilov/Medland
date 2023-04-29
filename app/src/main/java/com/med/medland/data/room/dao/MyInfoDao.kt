package com.med.medland.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.med.medland.data.room.table.MyInfoTable

@Dao
interface MyInfoDao {

    @Query("SELECT * FROM MyInfoTable")
    fun getMyInfo() : MyInfoTable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMyInfo(myInfoTable: MyInfoTable)

}