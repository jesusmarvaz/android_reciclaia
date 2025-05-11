package com.ingencode.reciclaia.data.repositories

import android.net.Uri
import com.ingencode.reciclaia.domain.model.ClassificationModel
import com.ingencode.reciclaia.domain.model.ClassificationModel.ClassificationData
import com.ingencode.reciclaia.domain.model.WasteTagCategory
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

/**
Created with ❤ by jesusmarvaz on 2025-04-23.
 */

interface IAProviderInterface {
    suspend fun getClassificationFromInference(uri: Uri): ClassificationData
}

class IAProviderMockImp @Inject constructor() : IAProviderInterface {
    companion object {
        private fun getPredictionList(size: Int): ArrayList<ClassificationModel.ClassificationPrediction> {
            val tempClassificationList = arrayListOf<ClassificationModel.ClassificationPrediction>()
            (0 until size).forEach {
                val randomIndex = Random.nextInt(0, WasteTagCategory.entries.size - 1)
                val labelRandom = WasteTagCategory.entries.filter {
                    it.ordinal == randomIndex }
                tempClassificationList.add(ClassificationModel.ClassificationPrediction(labelRandom[0].tags.first().tag, Random.nextFloat()))
            }
            return tempClassificationList
        }
        private fun buildClassificationList(uri: Uri): ArrayList<ClassificationData> {
            val tempList: ArrayList<ClassificationData> = arrayListOf()
            (0 until 100).forEach { tempList.add(
                ClassificationData(predictions = getPredictionList(Random.nextInt(0,5)),
                    model = ClassificationModel.ModelInfo("mock_model", "4.5.6"), timestamp = System.currentTimeMillis())
            )}
            return tempList
        }
        fun classificationMock(uri: Uri): ClassificationData = buildClassificationList(uri)[Random.nextInt(0, 100)]

    }
    override suspend fun getClassificationFromInference(uri: Uri): ClassificationData {
        delay(2000)
        val classification = classificationMock(uri)
        if (classification.predictions.isEmpty()) throw IllegalArgumentException("no prediction")
        return classification
    }
}

//TODO class IAProviderLocal() : IAProviderInterface { override fun getClassificationFromInference(): ClassificationModel {} }
//TODO class IAProviderRemote() : IAProviderInterface { override fun getClassificationFromInference(): ClassificationModel {} }