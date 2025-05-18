package com.ingencode.reciclaia.domain.model

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-18.
 */
data class LearnModel(val wasteTypes: List<WasteType>, val processingTypes: List<ProcessingType>,
    val urls: List<UrlModel>)