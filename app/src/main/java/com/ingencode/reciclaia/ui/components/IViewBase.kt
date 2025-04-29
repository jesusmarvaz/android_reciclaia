package com.ingencode.reciclaia.ui.components

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner

/**
Created with ❤ by jesusmarvaz on 2025-04-26.
 */

interface IViewBase {
    fun getViewLifeCycleOwner(): LifecycleOwner
    fun getViewModelBase(): ViewModelBase?
    fun observeVM()
    fun getPb(): ProgressBar?
    fun getShaderLoading(): View?
    fun observeViewModelBase() {
        val vm = getViewModelBase() ?: return
        val pb = getPb()
        val sh = getShaderLoading()
        val lifecycleOwner = getViewLifeCycleOwner()
        vm.observableSealedError().observe(lifecycleOwner) { if (it != null) vm.onError() }
        vm.observableLoading().observe(lifecycleOwner) {
            if (it == false) {
                pb?.visibility = View.INVISIBLE
                sh?.visibility = View.GONE
            }
            else {
                pb?.visibility = View.VISIBLE
                sh?.visibility = View.VISIBLE
            }
        }

        /*vm.observableUser().observe(lifecycleOwner) {
            if (it != null) vm.saveUserInCache(it)
        }*/
    }
}