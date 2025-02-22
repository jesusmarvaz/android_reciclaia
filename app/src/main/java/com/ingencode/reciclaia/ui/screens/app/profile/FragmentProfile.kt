package com.ingencode.reciclaia.ui.screens.app.profile

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.databinding.FragmentProfileBinding
import com.ingencode.reciclaia.ui.components.FragmentBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.nameClass

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

class FragmentProfile : FragmentBaseForViewmodel() {
    private lateinit var binding: FragmentProfileBinding
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
        binding.title.tvScreenTitle.text = getString(R.string.profile)
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding
    }
}