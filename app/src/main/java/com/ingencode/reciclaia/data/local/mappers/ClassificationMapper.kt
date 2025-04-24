package com.ingencode.reciclaia.data.local.mappers

import androidx.core.net.toUri
import com.ingencode.reciclaia.data.local.converters.ClassificationConverter
import com.ingencode.reciclaia.data.local.entities.ClassificationEntity
import com.ingencode.reciclaia.domain.model.ClassificationModel

/**
Created with ❤ by jesusmarvaz on 2025-04-21.
 */

fun ClassificationModel.toEntity(): ClassificationEntity {
    return ClassificationEntity(id = this.id,
        uri = this.uri.toString(),
        predictions = ClassificationConverter().fromPredictions(this.predictions)?: "",
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

fun ClassificationEntity.toModel(): ClassificationModel {
    val conv = ClassificationConverter()
    val modelInfo = if (this.modelName == null || this.modelName?.isEmpty() != false)
        null
    else
        ClassificationModel.ModelInfo(this.modelName!!, this.modelVersion)

    val builder = ClassificationModel.Builder(
        this.uri.toUri())
        .predictions(conv.toPredictions(this.predictions) ?: arrayListOf())
        .model(modelInfo)
        .timestamp(this.timestamp)
        .title(this.title)
        .comments(this.comments)
    if (this.latitude != null && this.longitude != null) builder.location(lat = this.latitude!!, long = this.longitude!!, provider = this.provider)
    return builder.build()
}