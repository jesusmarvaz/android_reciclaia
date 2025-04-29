package com.ingencode.reciclaia.ui.components

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.ui.viewmodels.SettingsViewModel
import com.ingencode.reciclaia.utils.ILog
import com.ingencode.reciclaia.utils.applyTheme
import kotlin.getValue

/**
Created with ❤ by jesusmarvaz on 2025-04-26.
 */

abstract class ActivityBaseForViewmodel(): AppCompatActivity(), IViewBase, ILog {
    abstract fun goBack()
    abstract fun getTheTag(): String
    abstract fun initProperties()
    abstract fun getInflatedViewBinding(): ViewBinding
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getInflatedViewBinding().root)
        initProperties()
        observeVM()
        observeViewModelBase()
        val theme = settingsViewModel.getThemeMode()
        applyTheme(theme)
    }

    override fun theTag(): String = getTheTag()
}