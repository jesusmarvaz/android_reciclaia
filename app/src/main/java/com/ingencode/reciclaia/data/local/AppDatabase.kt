package com.ingencode.reciclaia.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ingencode.reciclaia.data.local.converters.ClassificationConverter
import com.ingencode.reciclaia.data.local.dao.ClassificationDao
import com.ingencode.reciclaia.data.local.entities.ClassificationEntity

/**
Created with ❤ by jesusmarvaz on 2025-04-20.
 */

@Database(entities = [ClassificationEntity::class], version = 1)
@TypeConverters(ClassificationConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun processedImageDao(): ClassificationDao
}