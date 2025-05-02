package com.ingencode.reciclaia.domain.model

import android.location.Location
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.ingencode.reciclaia.utils.sha256
import java.io.Serializable

/**
Created with ❤ by jesusmarvaz on 2025-04-15.
 */

/*class ClassificationModel private constructor(uri: Uri): Serializable {
    var id: String = uri.toString().sha256(); private set
    var uri: Uri = uri
        set(value) { this.id = value.toString().sha256() ; field = value}
    var predictions: ArrayList<ClassificationPrediction> = arrayListOf<ClassificationPrediction>(); private set
    var model: ModelInfo? = null; private set
    var timestamp: Long? = null; private set
    var title: String? = null
    var comments: String? = null
    var location: Location? = null

    val topPrediction: ClassificationPrediction?
        get() = predictions.maxByOrNull { it.confidence }

    fun addPrediction(prediction: ClassificationPrediction) = this.predictions.add(prediction)

    data class ClassificationPrediction(val label: String, val confidence: Float): Serializable
    data class ModelInfo(val modalName:String, val modelVersion: String? = null): Serializable
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
}*/

data class ClassificationModel (
    var uri: Uri, var predictions: ArrayList<ClassificationPrediction> = arrayListOf<ClassificationPrediction>(),
    var model: ModelInfo? = null,
    var timestamp: Long? = null,
    var title: String? = null,
    var comments: String? = null,
    var location: Location? = null): Serializable {
        fun getShaID() = uri.toString().sha256()
    val topPrediction: ClassificationPrediction?
        get() = predictions.maxByOrNull { it.confidence }

    fun addPrediction(prediction: ClassificationPrediction) = this.predictions.add(prediction)

    data class ClassificationPrediction(val label: String, val confidence: Float): Serializable
    data class ModelInfo(val modalName:String, val modelVersion: String? = null): Serializable

}


fun ClassificationModel.ClassificationPrediction.toText(): String {
    return "$label | ${"%.2f".format(confidence)}"
}

fun ClassificationModel.ModelInfo.toText(): String {
    return "$modalName${if(!modelVersion.isNullOrEmpty()) " | version:$modelVersion" else ""}"
}

fun ClassificationModel.predictionsToText(): String {
    if (predictions.isEmpty()) return ""
    var predictionsText = StringBuffer()
    predictions.forEachIndexed { index, prediction ->
        predictionsText.append(prediction.toText())
        if (index != predictions.size - 1) {
            predictionsText.append("\n")
        }
    }
    return predictionsText.toString()
}