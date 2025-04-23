package com.ingencode.reciclaia.data.local.mappers

import androidx.core.net.toUri
import com.ingencode.reciclaia.data.local.converters.ProcessedImageConverter
import com.ingencode.reciclaia.data.local.entities.ProcessedImageEntity
import com.ingencode.reciclaia.domain.model.ProcessedImageModel

/**
Created with ❤ by jesusmarvaz on 2025-04-21.
 */

fun ProcessedImageModel.toEntity(): ProcessedImageEntity {
    return ProcessedImageEntity(id = this.id,
        uri = this.uri.toString(),
        predictions = ProcessedImageConverter().fromPredictions(this.predictions)?: "",
        modelName = this.model?.modalName,
        modelVersion = this.model?.modelVersion,
        timestamp = this.timestamp,
        title = this.title,
        comments = this.comments,
        latitude = this.location?.latitude,
        longitude = this.location?.longitude,
        provider = this.location?.provider
    )
}

fun ProcessedImageEntity.toModel(): ProcessedImageModel {
    val conv = ProcessedImageConverter()
    val modelInfo = if (this.modelName == null || this.modelName?.isEmpty() != false)
        null
    else
        ProcessedImageModel.ModelInfo(this.modelName!!, this.modelVersion)

    val builder = ProcessedImageModel.Builder(
        this.uri.toUri())
        .predictions(conv.toPredictions(this.predictions) ?: arrayListOf())
        .model(modelInfo)
        .timestamp(this.timestamp)
        .title(this.title)
        .comments(this.comments)
    if (this.latitude != null && this.longitude != null) builder.location(lat = this.latitude!!, long = this.longitude!!, provider = this.provider)
    return builder.build()
}