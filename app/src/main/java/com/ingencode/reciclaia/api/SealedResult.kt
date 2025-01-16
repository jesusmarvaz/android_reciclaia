package com.ingencode.reciclaia.api

import com.ingencode.reciclaia.common.SealedError

/**
Created with ❤ by jesusmarvaz on 2025-01-13.
 */

sealed class SealedResult<out T> {
    data class ResultSuccess<T>(val data: T) : SealedResult<T>()
    data class ResultError(val error: SealedError) : SealedResult<Nothing>()
}
