package com.ingencode.reciclaia.domain.model

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-18.
 */

sealed class LearnModelBundle {
    data class TitleItem(val title: String) : LearnModelBundle()
    data class WasteItem(val wasteTypeModel: WasteType) : LearnModelBundle()
    data class ProcessingItem(val processingInfoModel: ProcessingType) : LearnModelBundle()
    data class UrlItem(val urlType: UrlModel) : LearnModelBundle()
}