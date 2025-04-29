package com.ingencode.reciclaia.domain.model

import android.location.Location
import android.net.Uri
import com.ingencode.reciclaia.utils.sha256

/**
Created with ❤ by jesusmarvaz on 2025-04-15.
 */

class ClassificationModel private constructor(uri: Uri) {
    var id: String = uri.toString().sha256(); private set
    var uri: Uri = uri; private set
    var predictions: ArrayList<ClassificationPrediction> = arrayListOf<ClassificationPrediction>(); private set
    var model: ModelInfo? = null; private set
    var timestamp: Long? = null; private set
    var title: String? = null; private set
    var comments: String? = null; private set
    var location: Location? = null; private set
    fun addPrediction(prediction: ClassificationPrediction) = this.predictions.add(prediction)
    data class ClassificationPrediction(val label: String, val confidence: Float)
    data class ModelInfo(val modalName:String, val modelVersion: String? = null)
    class Builder(uri: Uri) {
        private val classificationModel = ClassificationModel(uri)
        fun predictions(predictions: ArrayList<ClassificationPrediction>) = apply {
            classificationModel.predictions.clear()
            classificationModel.predictions.addAll(predictions)
        }

        fun model(model: ModelInfo?) = apply { classificationModel.model = model }
        fun timestamp(timeStamp: Long?) = apply { classificationModel.timestamp = timeStamp }
        fun title(title: String?) = apply { classificationModel.title }
        fun comments(comments: String?) = apply { classificationModel.comments }
        fun location(lat: Double, long: Double, provider: String?) = apply { classificationModel.location = Location(provider).apply { latitude = lat; longitude = long } }
        fun build(): ClassificationModel = classificationModel
    }
}