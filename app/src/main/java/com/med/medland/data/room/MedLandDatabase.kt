package com.med.medland.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.crud.data.local.ChatDao
import uz.crud.data.model.ChatData

@Database(
    entities = [ChatData::class],
    version = 1,
    exportSchema = false
)
abstract class MedLandDatabase: RoomDatabase() {

    abstract fun chatDao(): ChatDao
}