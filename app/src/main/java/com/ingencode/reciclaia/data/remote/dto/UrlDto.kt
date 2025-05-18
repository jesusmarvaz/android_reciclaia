package com.ingencode.reciclaia.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-18.
 */
data class UrlDTO(
    @SerializedName("url")
    val url: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String
)

