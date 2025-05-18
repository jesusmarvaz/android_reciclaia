package com.ingencode.reciclaia.ui.screens.app.history

import com.ingencode.reciclaia.domain.model.WasteProcessing

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-13.
 */
interface IProcessingInfoListener {
    fun onInfoClicked(wasteProcessing: WasteProcessing)
}