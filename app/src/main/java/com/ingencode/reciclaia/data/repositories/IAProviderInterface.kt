package com.ingencode.reciclaia.data.repositories

import androidx.core.net.toUri
import com.ingencode.reciclaia.domain.model.ClassificationModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

/**
Created with ❤ by jesusmarvaz on 2025-04-23.
 */

interface IAProviderInterface {
    suspend fun getClassificationFromInference(): ClassificationModel
}

class IAProviderMockImp @Inject constructor() : IAProviderInterface {
    companion object {
        private fun getPredictionList(size: Int): ArrayList<ClassificationModel.ClassificationPrediction> {
            val tempClassificationList = arrayListOf<ClassificationModel.ClassificationPrediction>()
            (0 until size).forEach {
                tempClassificationList.add(ClassificationModel.ClassificationPrediction("label random:${Random.nextInt(0,2000)}", Random.nextFloat()))
            }
            return tempClassificationList
        }
        private val classificationList: ArrayList<ClassificationModel> by lazy(::buildClassificationList)
        private fun buildClassificationList(): ArrayList<ClassificationModel> {
            val tempList: ArrayList<ClassificationModel> = arrayListOf()
            (0 until 100).forEach { tempList.add(
                ClassificationModel.Builder("uri_mock_${it}".toUri())
                    .predictions(getPredictionList(Random.nextInt(0,5)))
                    .title(title = "title_${it}")
                    .comments(comments = "comments_${it}")
                    .location(Random.nextDouble(), Random.nextDouble(), "providerRandom:${Random.nextInt(0,1000)}")
                    .timestamp(System.currentTimeMillis())
                    .model(ClassificationModel.ModelInfo("mock_model(no model)"))
                    .build()
            ) }
            return tempList
        }
        fun classificationMock(): ClassificationModel = buildClassificationList()[Random.nextInt(0, 100)]

    }
    override suspend fun getClassificationFromInference(): ClassificationModel {
        delay(2000)
        return classificationMock()
    }
}

//TODO class IAProviderLocal() : IAProviderInterface { override fun getClassificationFromInference(): ClassificationModel {} }
//TODO class IAProviderRemote() : IAProviderInterface { override fun getClassificationFromInference(): ClassificationModel {} }