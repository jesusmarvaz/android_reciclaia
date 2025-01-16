package com.ingencode.reciclaia.entities.dto

/**
Created with ❤ by jesusmarvaz on 2025-01-13.
 */

data class ErrorDTO(val code:Int? = null,
                    val message: String? = null,
                    val type:String? = null,
                    val error:Boolean? = null
): DTO