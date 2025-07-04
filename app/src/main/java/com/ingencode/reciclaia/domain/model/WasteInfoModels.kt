package com.ingencode.reciclaia.domain.model

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-18.
 */

data class ProcessingType(
    val imageUri: String,
    val processingName: ProcessingNameModel,
    val description: String
)

data class ProcessingNameModel(
    val name: String,
    val url: String
)

data class WasteType(
    val wasteType: String,
    val description: String
)

data class UrlModel(
    val url: String,
    val title: String,
    val description: String
)