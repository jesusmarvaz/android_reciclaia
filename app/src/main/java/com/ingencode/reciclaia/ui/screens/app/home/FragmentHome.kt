package com.ingencode.reciclaia.ui.screens.app.home

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.databinding.FragmentHomeBinding
import com.ingencode.reciclaia.ui.components.FragmentBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.ui.screens.app.history.HistoryViewmodel
import com.ingencode.reciclaia.utils.nameClass
import kotlin.getValue

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
        binding.title.tvScreenTitle.text = getString(R.string.home)
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding
    }
}