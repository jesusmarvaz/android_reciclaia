package com.ingencode.reciclaia.ui.screens.app

import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.databinding.FragmentAppComposeBinding
import com.ingencode.reciclaia.ui.components.FragmentBase
import com.ingencode.reciclaia.utils.nameClass

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

class FragmentAppComposeVersion : FragmentBase() {
    private lateinit var binding: FragmentAppComposeBinding

    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass

    override fun initProperties() {
        binding.appComposeVersion.setContent {  }
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentAppComposeBinding.inflate(layoutInflater)
        return binding
    }
}