package com.ingencode.reciclaia.ui.screens.app.home

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.databinding.FragmentHomeBinding
import com.ingencode.reciclaia.ui.components.FragmentBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.nameClass

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

class FragmentHome : FragmentBaseForViewmodel() {
    private lateinit var binding: FragmentHomeBinding
    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner
    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass

    override fun getViewModelBase(): ViewModelBase? {
        //TODO("Not yet implemented")
        return null
    }

    override fun observeVM() {
        //TODO("Not yet implemented")
    }

    override fun getPb(): ProgressBar? {
        //TODO("Not yet implemented")
        return null
    }

    override fun getShaderLoading(): View? {
        //TODO("Not yet implemented")
        return null
    }



    override fun initProperties() {
        //TODO("Not yet implemented")
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding
    }
}