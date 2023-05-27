package com.med.medland.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.med.medland.data.room.table.MyInfoTable

@Dao
interface MyInfoDao {

    @Query("SELECT * FROM MyInfoTable WHERE myId LIKE :myId LIMIT 1")
    fun getMyInfo(myId : Int) : MyInfoTable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMyInfo(myInfoTable: MyInfoTable)

    @Update(entity = MyInfoTable::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateMyInfo(myInfoTable: MyInfoTable)

}