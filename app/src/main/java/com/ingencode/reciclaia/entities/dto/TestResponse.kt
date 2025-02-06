package com.ingencode.reciclaia.entities.dto

import com.google.gson.annotations.SerializedName

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-03.
 */
data class TestResponse(@SerializedName("test_response_string") val testString: String)
data class TestResponseDb(@SerializedName("nombre") val name: String,
    @SerializedName("edad") val age: Int)
