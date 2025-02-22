package com.ingencode.reciclaia.ui.screens.app.settings

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.databinding.FragmentSettingsBinding
import com.ingencode.reciclaia.ui.components.FragmentBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.nameClass

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

class FragmentSettings : FragmentBaseForViewmodel() {
    private lateinit var binding: FragmentSettingsBinding
    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner
    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass
    override fun getViewModelBase(): ViewModelBase? = null
    override fun observeVM() {}
    override fun getPb(): ProgressBar? = null
    override fun getShaderLoading(): View? = null

    override fun initProperties() {
        binding.title.tvScreenTitle.text = getString(R.string.settings)

    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding
    }
}