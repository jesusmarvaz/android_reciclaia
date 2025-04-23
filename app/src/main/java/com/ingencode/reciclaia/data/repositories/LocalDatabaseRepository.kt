package com.ingencode.reciclaia.data.repositories

import com.ingencode.reciclaia.data.local.dao.ProcessedImageDao
import com.ingencode.reciclaia.domain.model.ProcessedImageModel
import com.ingencode.reciclaia.data.local.mappers.toEntity
import com.ingencode.reciclaia.data.local.mappers.toModel
import javax.inject.Inject

/**
Created with ❤ by jesusmarvaz on 2025-04-20.
 */

interface IProcessedImageModelRepository {
    fun insert(model: ProcessedImageModel)
    fun insertAll(list: List<ProcessedImageModel>)
    fun getProcessedImageById(id:Int): ProcessedImageModel
    fun getAllProcessedImages(): List<ProcessedImageModel>
    fun deleteAllProcessedImages(): Int
    fun deleteById(id:Int): Int
    fun updateProcessedImageById(model: ProcessedImageModel)
}

class ProcessedImageModelRepositoryImpl @Inject constructor(private val processedImageDao: ProcessedImageDao): IProcessedImageModelRepository {
    override fun insert(model: ProcessedImageModel) =
        processedImageDao.insert(model.toEntity())

    override fun insertAll(list: List<ProcessedImageModel>) =
        processedImageDao.insertAll(list.map { it.toEntity() })

    override fun getProcessedImageById(id: Int): ProcessedImageModel =
        processedImageDao.getProcessedImageById(id).toModel()

    override fun getAllProcessedImages(): List<ProcessedImageModel> =
        processedImageDao.getAllProcessedImages().map { it.toModel() }

    override fun deleteAllProcessedImages(): Int =
        processedImageDao.deleteAllProcessedImages()

    override fun deleteById(id: Int): Int =
        processedImageDao.deleteById(id)

    override fun updateProcessedImageById(model: ProcessedImageModel) =
        processedImageDao.updateProcessedImageById(model.toEntity())
}