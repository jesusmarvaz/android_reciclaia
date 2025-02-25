package com.ingencode.reciclaia.utils

import androidx.appcompat.app.AppCompatDelegate
import com.ingencode.reciclaia.data.repositories.ISettingsRepository

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-24.
 */

fun applyTheme(theme: String) {
    val mode = when (theme) {
        ISettingsRepository.system -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        ISettingsRepository.light -> AppCompatDelegate.MODE_NIGHT_NO
        ISettingsRepository.dark -> AppCompatDelegate.MODE_NIGHT_YES
        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
    AppCompatDelegate.setDefaultNightMode(mode)
}