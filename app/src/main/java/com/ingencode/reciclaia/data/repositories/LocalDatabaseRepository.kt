package com.ingencode.reciclaia.data.repositories

import com.ingencode.reciclaia.data.local.dao.ClassificationDao
import com.ingencode.reciclaia.data.local.mappers.toEntity
import com.ingencode.reciclaia.data.local.mappers.toModel
import com.ingencode.reciclaia.domain.model.ClassificationModel
import javax.inject.Inject

/**
Created with ❤ by jesusmarvaz on 2025-04-20.
 */

interface IClassificationRepository {
    fun insert(model: ClassificationModel)
    fun insertAll(list: List<ClassificationModel>)
    fun getClassificationsById(id:Int): ClassificationModel
    fun getAllProcessedImages(): List<ClassificationModel>
    fun deleteAllProcessedImages(): Int
    fun deleteById(id:Int): Int
    fun updateProcessedImageById(model: ClassificationModel)
}

class ClassificationRepositoryImpl @Inject constructor(private val classificationDao: ClassificationDao): IClassificationRepository {
    override fun insert(model: ClassificationModel) =
        classificationDao.insert(model.toEntity())

    override fun insertAll(list: List<ClassificationModel>) =
        classificationDao.insertAll(list.map { it.toEntity() })

    override fun getClassificationsById(id: Int): ClassificationModel =
        classificationDao.getById(id).toModel()

    override fun getAllProcessedImages(): List<ClassificationModel> =
        classificationDao.getAll().map { it.toModel() }

    override fun deleteAllProcessedImages(): Int =
        classificationDao.deleteAll()

    override fun deleteById(id: Int): Int =
        classificationDao.deleteById(id)

    override fun updateProcessedImageById(model: ClassificationModel) =
        classificationDao.update(model.toEntity())
}