package com.ingencode.reciclaia.data.repositories

import com.ingencode.reciclaia.data.local.ProcessedImageDao
import com.ingencode.reciclaia.data.remote.api.SealedResult
import com.ingencode.reciclaia.domain.model.ProcessedImageModel
import javax.inject.Inject

/**
Created with ❤ by jesusmarvaz on 2025-04-20.
 */

interface IProcessedImageModelRepository {
    fun insert(model: ProcessedImageModel)
    fun insertAll(list: List<ProcessedImageModel>)
    fun getProcessedImageById(id:Int): ProcessedImageModel
    fun getAllProcessedImages(): List<ProcessedImageModel>
    fun deleteAllProcessedImages()
    fun deleteById(id:Int): ProcessedImageModel
    fun updateProcessedImageById(processedImageModel: ProcessedImageModel)
}

class ProcessedImageModelRepositoryImpl @Inject constructor(private val processedImageDao: ProcessedImageDao): IProcessedImageModelRepository {
    override fun insert(model: ProcessedImageModel) {
        TODO("Not yet implemented")
    }

    override fun insertAll(list: List<ProcessedImageModel>) {
        TODO("Not yet implemented")
    }

    override fun getProcessedImageById(id: Int): ProcessedImageModel {
        TODO("Not yet implemented")
    }

    override fun getAllProcessedImages(): List<ProcessedImageModel> {
        TODO("Not yet implemented")
    }

    override fun deleteAllProcessedImages() {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Int): ProcessedImageModel {
        TODO("Not yet implemented")
    }

    override fun updateProcessedImageById(processedImageModel: ProcessedImageModel) {
        TODO("Not yet implemented")
    }

}