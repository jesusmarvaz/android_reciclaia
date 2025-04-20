package com.ingencode.reciclaia.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ingencode.reciclaia.domain.model.ProcessedImageModel

/**
Created with ❤ by jesusmarvaz on 2025-04-20.
 */

@Database(entities = [ProcessedImageEntit::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
}