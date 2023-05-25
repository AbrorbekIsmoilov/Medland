package com.med.medland.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.med.medland.data.room.dao.MyInfoDao
import com.med.medland.data.room.table.MyInfoTable

@Database(entities = [MyInfoTable :: class], version = 1)
abstract class UserDataBase : RoomDatabase() {

    abstract fun myInfoDao() : MyInfoDao

    companion object {
        @Volatile
        private var INSTANCE : UserDataBase? = null

        fun getDataBase(context: Context) : UserDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDataBase :: class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}