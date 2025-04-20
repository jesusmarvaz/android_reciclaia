package com.ingencode.reciclaia.data.local

import androidx.room.Dao
import androidx.room.Insert
import com.ingencode.reciclaia.domain.model.ProcessedImageModel

/**
Created with ❤ by jesusmarvaz on 2025-04-20.
 */

@Dao
interface ProcessedImageDao {
    @Insert
    fun insert(processedImageEntity: ProcessedImageEntity)
}