package com.ingencode.reciclaia.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ingencode.reciclaia.data.local.converters.ProcessedImageConverter
import com.ingencode.reciclaia.data.local.dao.ProcessedImageDao
import com.ingencode.reciclaia.data.local.entities.ProcessedImageEntity

/**
Created with ❤ by jesusmarvaz on 2025-04-20.
 */

@Database(entities = [ProcessedImageEntity::class], version = 1)
@TypeConverters(ProcessedImageConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun processedImageDao(): ProcessedImageDao
}