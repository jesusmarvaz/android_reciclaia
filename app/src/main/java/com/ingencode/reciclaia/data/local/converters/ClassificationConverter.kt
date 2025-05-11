package com.ingencode.reciclaia.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ingencode.reciclaia.domain.model.ClassificationModel

/**
Created with ❤ by jesusmarvaz on 2025-04-22.
 */

class ClassificationConverter {
    @TypeConverter
    fun toPredictions(predictionsString: String?): ArrayList<ClassificationModel.ClassificationPrediction>? {
        if (predictionsString == null) return null
        val type = object : TypeToken<ArrayList<ClassificationModel.ClassificationPrediction>>(){}.type
        return Gson().fromJson(predictionsString, type)
    }

    @TypeConverter
    fun fromPredictions(predictions: ArrayList<ClassificationModel.ClassificationPrediction>?): String? =
        Gson().toJson(predictions)
}