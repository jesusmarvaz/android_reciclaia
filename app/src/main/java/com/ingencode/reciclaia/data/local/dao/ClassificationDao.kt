package com.ingencode.reciclaia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ingencode.reciclaia.data.local.entities.ClassificationEntity

/**
Created with ❤ by jesusmarvaz on 2025-04-20.
 */

@Dao
interface ClassificationDao {
    @Insert
    fun insert(processedImage: ClassificationEntity)
    @Insert
    fun insertAll(list: List<ClassificationEntity>)
    @Query("SELECT * FROM ClassificationEntity WHERE id = :id")
    fun getById(id: String): ClassificationEntity?
    @Query("SELECT * FROM ClassificationEntity")
    fun getAll(): List<ClassificationEntity>?
    @Query("DELETE FROM ClassificationEntity")
    fun deleteAll(): Int
    @Query("DELETE FROM ClassificationEntity WHERE id =:id")
    fun deleteById(id: String): Int
    @Update
    fun update(processedImage: ClassificationEntity)
}