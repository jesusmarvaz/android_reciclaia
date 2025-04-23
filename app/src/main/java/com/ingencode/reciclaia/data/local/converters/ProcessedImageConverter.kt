package com.ingencode.reciclaia.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ingencode.reciclaia.domain.model.ProcessedImageModel

/**
Created with ❤ by jesusmarvaz on 2025-04-22.
 */

class ProcessedImageConverter {
    @TypeConverter
    fun toPredictions(predictionsString: String?): ArrayList<ProcessedImageModel.ClassificationPrediction>? {
        if (predictionsString == null) return null
        val type = object : TypeToken<ProcessedImageModel.ClassificationPrediction>(){}.type
        return Gson().fromJson(predictionsString, type)
    }

    @TypeConverter
    fun fromPredictions(predictions: ArrayList<ProcessedImageModel.ClassificationPrediction>?): String? =
        Gson().toJson(predictions)
}