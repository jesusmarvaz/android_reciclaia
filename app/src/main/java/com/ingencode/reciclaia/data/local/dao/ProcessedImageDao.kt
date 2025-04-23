package com.ingencode.reciclaia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ingencode.reciclaia.data.local.entities.ProcessedImageEntity

/**
Created with ❤ by jesusmarvaz on 2025-04-20.
 */

@Dao
interface ProcessedImageDao {
    @Insert
    fun insert(processedImage: ProcessedImageEntity)
    @Insert
    fun insertAll(list: List<ProcessedImageEntity>)
    @Query("SELECT * FROM ProcessedImageEntity WHERE id = :id")
    fun getProcessedImageById(id:Int): ProcessedImageEntity
    @Query("SELECT * FROM ProcessedImageEntity")
    fun getAllProcessedImages(): List<ProcessedImageEntity>
    @Query("DELETE FROM ProcessedImageEntity")
    fun deleteAllProcessedImages(): Int
    @Query("DELETE FROM ProcessedImageEntity WHERE id =:id")
    fun deleteById(id:Int): Int
    @Update
    fun updateProcessedImageById(processedImage: ProcessedImageEntity)
}