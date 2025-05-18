package com.ingencode.reciclaia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-18.
 */
data class WasteTypeDto(
    @SerializedName("waste_type")
    val wasteType: String,

    @SerializedName("description")
    val description: String
)

