package com.ingencode.reciclaia.common

import android.util.Log

/**
Created with ❤ by jesusmarvaz on 2025-01-13.
 */

interface ILog {
    fun logDebug(message: String) = Log.d(theTag(), message)
    fun logError(message: String) = Log.e(theTag(), message)
    fun theTag(): String
}