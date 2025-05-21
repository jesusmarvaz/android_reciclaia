package com.ingencode.reciclaia.domain.model

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import com.ingencode.reciclaia.R
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

data class ClassificationModel(
    var uri: Uri,
    var classificationData: ClassificationData? = null,
    var title: String? = null,
    var comments: String? = null,
    var location: Location? = null, //latitude, longitude
) : Serializable {

    fun getShaID() = uri.toString().sha256()
    fun findCategories(): Set<WasteTagCategory>? {
        return WasteTagCategory.entries
            .filter { tag -> tag.tags.any { it.tag.contains(classificationData?.topPrediction?.label ?: "", ignoreCase = true) } }
            .toSet()
    }

    fun getClassificationAndProcessing(context: Context): String? {
        val tag = findCategories()?.first()?.tags?.first()?.idStringName ?: return null
        val processingId = findCategories()?.first()?.processing?.first()?.idStringTitle ?: return null
        val confidence = this.classificationData?.topPrediction?.confidence ?: return null
        val text = context.getString(R.string.pattern_prediction_and_processing).format(context.getString(tag), confidence, context.getString(processingId).toLowerCase(
            Locale.current))
        return text
    }

    data class ClassificationPrediction(val label: String, val confidence: Float) : Serializable
    data class ClassificationData(
        var predictions: ArrayList<ClassificationPrediction> = arrayListOf<ClassificationPrediction>(),
        var model: ModelInfo? = null,
        var timestamp: Long? = null
    ) : Serializable {
        val topPrediction: ClassificationPrediction?
            get() = predictions.maxByOrNull { it.confidence }
        fun textColor(): Int? {
            val c = topPrediction?.confidence ?: return null
            return when {
                (c < 0.25 && c >= 0) -> {
                    R.color.confidence_25
                }

                (c >= 0.25 && c < 0.5) -> {
                    R.color.confidence_50
                }

                (c >= 0.5 && c < 0.7) -> {
                    R.color.confidence_70
                }

                (c >= 0.7 && c < 0.85) -> {
                    R.color.confidence_85
                }

                (c >= 0.85 && c <= 1) -> {
                    R.color.confidence_100
                }

                else -> null
            }
        }
        fun backgroundColor(): Int? {
            val c = topPrediction?.confidence ?: return null
            return when {
                (c < 0.25 && c >= 0) -> {
                    R.color.confidence_25_background
                }

                (c >= 0.25 && c < 0.5) -> {
                    R.color.confidence_50_background
                }

                (c >= 0.5 && c < 0.7) -> {
                    R.color.confidence_70_background
                }

                (c >= 0.7 && c < 0.85) -> {
                    R.color.confidence_85_background
                }

                (c >= 0.85 && c <= 1) -> {
                    R.color.confidence_100_background
                }

                else -> null
            }
        }
    }

    data class ModelInfo(val modelName: String, val modelVersion: String? = null) : Serializable
    data class Location(val latitude: Double, val longitude: Double) : Serializable

}


fun ClassificationModel.ClassificationPrediction.toText(): String {
    return "$label | ${"%.2f".format(confidence)}"
}

fun ClassificationModel.ModelInfo.toText(): String {
    return "$modelName${if (!modelVersion.isNullOrEmpty()) " | version:$modelVersion" else ""}"
}

fun ClassificationModel.ClassificationData.predictionsToText(): String {
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

fun ClassificationModel.isAppUri(context: Context): Boolean {
    //return inputUri.authority == APP_FILE_PROVIDER_AUTHORITY
    if (uri.scheme == "file") {
        val path = uri.path
        if (path != null) {
            // Check if the path falls within your app's internal or external private storage
            val internalDirs = listOf(
                context.filesDir,
                context.cacheDir,
                context.noBackupFilesDir
            )
            val externalFilesDir =
                context.getExternalFilesDir(null) // Or a more specific type, if needed

            if (internalDirs.any { path.startsWith(it?.absolutePath ?: "") }) {
                Log.d("UriCheck", "Uri points to app's internal storage: $uri")
                return true
            }

            if (externalFilesDir != null && path.startsWith(externalFilesDir.absolutePath)) {
                Log.d("UriCheck", "Uri points to app's external private storage: $uri")
                return true
            }
        }
    }

    Log.d("UriCheck", "Uri is NOT from app's private storage: $uri")
    return false // It's not from your app's storage
}