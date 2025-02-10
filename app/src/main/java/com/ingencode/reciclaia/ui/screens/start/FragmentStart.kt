package com.ingencode.reciclaia.ui.screens.start

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.databinding.FragmentStartBinding
import com.ingencode.reciclaia.ui.components.FragmentBase
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.nameClass

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-07.
 */

class FragmentStart: FragmentBase() {
    private lateinit var binding: FragmentStartBinding

    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass
    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner
    override fun getViewModelBase(): ViewModelBase? = null

    override fun initProperties() {
        TODO("Not yet implemented")
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
}