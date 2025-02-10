package com.ingencode.reciclaia.data.remote.api

import com.ingencode.reciclaia.utils.SealedError

/**
Created with ❤ by jesusmarvaz on 2025-01-13.
 */

sealed class SealedResult<out T> {
    data class ResultSuccess<T>(val data: T) : SealedResult<T>()
    data class ResultError(val error: SealedError) : SealedResult<Nothing>()
}
