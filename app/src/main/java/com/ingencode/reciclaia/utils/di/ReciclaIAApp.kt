package com.ingencode.reciclaia.utils.di

import android.app.Application
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-02.
 */
@HiltAndroidApp
class ReciclaIAApp: Application() {
    override fun onCreate() {
        DynamicColors.applyToActivitiesIfAvailable(this)
        super.onCreate()
    }
}