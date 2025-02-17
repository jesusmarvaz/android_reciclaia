package com.ingencode.reciclaia.ui.screens.keycolors

import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.databinding.FragmentKeycolorsBinding
import com.ingencode.reciclaia.ui.components.FragmentBase
import com.ingencode.reciclaia.utils.nameClass

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-17.
 */
class KeyColorsFragment : FragmentBase() {
    private lateinit var binding: FragmentKeycolorsBinding

    override fun goBack() { findNavController().popBackStack() }
    override fun getFragmentTag(): String = this.nameClass
    override fun initProperties() {}

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentKeycolorsBinding.inflate(layoutInflater)
        return binding
    }

}