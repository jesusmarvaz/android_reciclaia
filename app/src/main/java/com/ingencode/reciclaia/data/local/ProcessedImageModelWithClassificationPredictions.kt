package com.ingencode.reciclaia.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.ingencode.reciclaia.domain.model.ProcessedImageModel

/**
Created with ❤ by jesusmarvaz on 2025-04-20.
 */

data class ProcessedImageModelWithClassificationPredictions(
    @Embedded val processedImage: ProcessedImageModel,
    @Relation(parentColumn = "id", entityColumn = "id") val predictions: List<ClassificationPrediction>
)
