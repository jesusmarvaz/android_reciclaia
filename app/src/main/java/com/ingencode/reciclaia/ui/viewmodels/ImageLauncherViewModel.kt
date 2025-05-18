package com.ingencode.reciclaia.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingencode.reciclaia.ui.screens.app.home.IImageSelector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
Created with ❤ by jesusmarvaz on 2025-05-16.
 */

@HiltViewModel
class ImageLauncherViewModel @Inject constructor(): ViewModel(), IImageSelector {
    private val _launchImageSelectorEvent = MutableSharedFlow<Unit>() // Usar SharedFlow para eventos
    val launchImageSelectorEvent = _launchImageSelectorEvent.asSharedFlow()

    override fun launchImageSelector() {
        viewModelScope.launch { _launchImageSelectorEvent.emit(Unit) }
    }
}