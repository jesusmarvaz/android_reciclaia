package com.ingencode.reciclaia.utils.di

import android.content.Context
import androidx.room.Room
import com.ingencode.reciclaia.data.local.AppDatabase
import com.ingencode.reciclaia.data.local.dao.ClassificationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
Created with ❤ by jesusmarvaz on 2025-04-21.
 */

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    }

    @Provides
    fun provideProcessedImageDao(database: AppDatabase): ClassificationDao {
        return database.processedImageDao()
    }
}