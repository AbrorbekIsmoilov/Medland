package com.med.medland.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.med.medland.data.room.MedLandDatabase
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @[AppScope Provides]
    fun provideDatabase(context: Context): MedLandDatabase {
        return Room.databaseBuilder(context, MedLandDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @[AppScope Provides]
    fun provideChatDao(appDatabase: MedLandDatabase) = appDatabase.chatDao()

    @[AppScope Provides]
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    companion object {
        const val NAME = "medLand_cache"
        const val DATABASE_NAME = "medLand_database"
    }
}