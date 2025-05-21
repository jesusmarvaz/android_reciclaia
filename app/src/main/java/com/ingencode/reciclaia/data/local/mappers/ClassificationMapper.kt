package com.ingencode.reciclaia.data.local.mappers

import androidx.core.net.toUri
import com.ingencode.reciclaia.data.local.converters.ClassificationConverter
import com.ingencode.reciclaia.data.local.entities.ClassificationEntity
import com.ingencode.reciclaia.domain.model.ClassificationModel

/**
Created with ❤ by jesusmarvaz on 2025-04-21.
 */

fun ClassificationModel.toEntity(): ClassificationEntity {
    return ClassificationEntity(id = this.getShaID(),
        uri = this.uri.toString(),
        predictions = ClassificationConverter().fromPredictions(this.classificationData?.predictions)?: "",
        modelName = this.classificationData?.model?.modelName,
        modelVersion = this.classificationData?.model?.modelVersion,
        timestamp = this.classificationData?.timestamp,
        title = this.title,
        comments = this.comments,
        latitude = this.location?.latitude,
        longitude = this.location?.longitude
    )
}

fun ClassificationEntity.toModel(): ClassificationModel {
    val conv = ClassificationConverter()
    val modelInfo = if (this.modelName == null || this.modelName?.isEmpty() != false)
        null
    else
        ClassificationModel.ModelInfo(this.modelName!!, this.modelVersion)

    val location = if(this.latitude == null || this.longitude == null) null else ClassificationModel.Location(
        latitude!!, longitude!!)
    val classificationData = ClassificationModel.ClassificationData(predictions = conv.toPredictions(this.predictions) ?: arrayListOf(),
        model = modelInfo, timestamp = this.timestamp)
    val model = ClassificationModel(uri = this.uri.toUri(), classificationData = classificationData, title = this.title, comments = this.comments,
        location = location)
    return model
}