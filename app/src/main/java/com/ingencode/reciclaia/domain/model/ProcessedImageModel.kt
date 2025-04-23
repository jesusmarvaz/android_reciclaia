package com.ingencode.reciclaia.domain.model

import android.location.Location
import android.net.Uri
import com.ingencode.reciclaia.utils.sha256

/**
Created with ❤ by jesusmarvaz on 2025-04-15.
 */

class ProcessedImageModel private constructor(uri: Uri) {
    val id: String = uri.toString().sha256(); private set
    val uri: Uri = uri; private set
    val predictions: ArrayList<ClassificationPrediction> = arrayListOf<ClassificationPrediction>(); private set
    var model: ModelInfo? = null; private set
    var timestamp: Long? = null; private set
    var title: String? = null; private set
    var comments: String? = null; private set
    var location: Location? = null; private set
    fun addPrediction(prediction: ClassificationPrediction) = this.predictions.add(prediction)
    data class ClassificationPrediction(val label: String, val confidence: Float)
    data class ModelInfo(val modalName:String, val modelVersion: String? = null)
    class Builder(uri: Uri) {
        private val processedImageModel = ProcessedImageModel(uri)
        fun predictions(predictions: ArrayList<ClassificationPrediction>) = apply {
            processedImageModel.predictions.clear()
            processedImageModel.predictions.addAll(predictions)
        }

        fun model(model: ModelInfo?) = apply { processedImageModel.model = model }
        fun timestamp(timeStamp: Long?) = apply { processedImageModel.timestamp = timeStamp }
        fun title(title: String?) = apply { processedImageModel.title }
        fun comments(comments: String?) = apply { processedImageModel.comments }
        fun location(lat: Double, long: Double, provider: String?) = apply { processedImageModel.location = Location(provider).apply { latitude = lat; longitude = long } }
        fun build(): ProcessedImageModel = processedImageModel
    }
}