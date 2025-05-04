package com.ingencode.reciclaia.data.repositories

import android.location.Location
import android.net.Uri
import androidx.core.net.toUri
import com.ingencode.reciclaia.domain.model.ClassificationModel
import kotlinx.coroutines.delay
import java.lang.IllegalArgumentException
import javax.inject.Inject
import kotlin.random.Random

/**
Created with ❤ by jesusmarvaz on 2025-04-23.
 */

interface IAProviderInterface {
    suspend fun getClassificationFromInference(uri: Uri): ClassificationModel
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
        private fun buildClassificationList(uri: Uri): ArrayList<ClassificationModel> {
            val tempList: ArrayList<ClassificationModel> = arrayListOf()
            (0 until 100).forEach { tempList.add(
                ClassificationModel(uri = uri, predictions = getPredictionList(Random.nextInt(0,5)),
                    model = ClassificationModel.ModelInfo("mock_model", "4.5.6"), timestamp = System.currentTimeMillis(),
                    title = "title_$it", comments = "comments_$it", location = Location("providerRandom:$Random").apply {
                        latitude = Random.nextDouble(-90.0, 90.0); longitude = Random.nextDouble(-180.0, 180.0) })
            ) }
            return tempList
        }
        fun classificationMock(uri: Uri): ClassificationModel = buildClassificationList(uri)[Random.nextInt(0, 100)]

    }
    override suspend fun getClassificationFromInference(uri: Uri): ClassificationModel {
        delay(2000)
        val classification = classificationMock(uri)
        if (classification.predictions.isEmpty()) throw IllegalArgumentException("no prediction")
        return classification
    }
}

//TODO class IAProviderLocal() : IAProviderInterface { override fun getClassificationFromInference(): ClassificationModel {} }
//TODO class IAProviderRemote() : IAProviderInterface { override fun getClassificationFromInference(): ClassificationModel {} }