package com.ingencode.reciclaia.ui.screens.initial

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.common.nameClass
import com.ingencode.reciclaia.ui.common.FragmentBase
import com.ingencode.reciclaia.ui.common.ViewModelBase

/**
 * Created with ❤ by Jesús Martín on 2025-01-12
 */

class FragmentInitial : FragmentBase() {
    override fun getFragmentTag(): String = this.nameClass

    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner

    override fun getViewModelBase(): ViewModelBase? = null

    override fun initProperties() {
        logDebug("TODO, iniciar propiedades")
    }

    override fun observeVM() {
        TODO("Not yet implemented")
    }

    override fun getInflatedViewBinding(): ViewBinding {
        TODO("Not yet implemented")
    }

    override fun getPb(): ProgressBar? {
        TODO("Not yet implemented")
    }

    override fun getShaderLoading(): View? {
        TODO("Not yet implemented")
    }

    override fun goBack() {
        TODO("Not yet implemented")
    }
}