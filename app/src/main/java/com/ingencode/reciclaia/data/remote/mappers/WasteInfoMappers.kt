package com.ingencode.reciclaia.data.remote.mappers

import com.ingencode.reciclaia.data.remote.api.Routes
import com.ingencode.reciclaia.data.remote.dto.ProcessingTypeDTO
import com.ingencode.reciclaia.data.remote.dto.UrlDTO
import com.ingencode.reciclaia.data.remote.dto.WasteTypeDto
import com.ingencode.reciclaia.domain.model.ProcessingType
import com.ingencode.reciclaia.domain.model.TitleModel
import com.ingencode.reciclaia.domain.model.UrlModel
import com.ingencode.reciclaia.domain.model.WasteType

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-18.
 */

fun WasteTypeDto.mapToModel(): WasteType =
    WasteType(wasteType = this.wasteType, description = this.description)


fun ProcessingTypeDTO.mapToModel(): ProcessingType = ProcessingType(
    imageUri = "${Routes.IMAGES_PATH}${this.imageUri}", title = TitleModel(
        name = this.title.name, url = this.title.url
    ), description = this.description
)

fun UrlDTO.mapToModel(): UrlModel =
    UrlModel(url = this.url, title = this.title, description = this.description)