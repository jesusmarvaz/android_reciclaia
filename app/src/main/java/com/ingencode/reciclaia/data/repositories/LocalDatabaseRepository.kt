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
    fun getClassificationsById(id:String): ClassificationModel?
    fun getAllProcessedImages(): List<ClassificationModel>?
    fun deleteAllProcessedImages(): Int
    fun deleteById(id:String): Int
    fun updateProcessedImage(model: ClassificationModel)
}

class ClassificationRepositoryImpl @Inject constructor(private val classificationDao: ClassificationDao): IClassificationRepository {
    override fun insert(model: ClassificationModel) =
        classificationDao.insert(model.toEntity())

    override fun insertAll(list: List<ClassificationModel>) =
        classificationDao.insertAll(list.map { it.toEntity() })

    override fun getClassificationsById(id: String): ClassificationModel? =
        classificationDao.getById(id)?.toModel()

    override fun getAllProcessedImages(): List<ClassificationModel>? {
        val list = classificationDao.getAll() ?: return null
        return classificationDao.getAll()?.map { it.toModel() }
    }

    override fun deleteAllProcessedImages(): Int =
        classificationDao.deleteAll()

    override fun deleteById(id: String): Int =
        classificationDao.deleteById(id)

    override fun updateProcessedImage(model: ClassificationModel) =
        with (classificationDao) {
            if (getClassificationsById(model.getShaID()) == null) {
                classificationDao.insert(model.toEntity())
            } else {
                classificationDao.update(model.toEntity())
            }
        }
}