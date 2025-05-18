package com.ingencode.reciclaia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-18.
 */

data class TitleDTO(
    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String
)

data class ProcessingTypeDTO(
    @SerializedName("image_uri")
    val imageUri: String,

    @SerializedName("title")
    val title: TitleDTO,

    @SerializedName("description")
    val description: String
)

