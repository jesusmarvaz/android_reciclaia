package com.ingencode.reciclaia.ui.screens.app

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.databinding.FragmentAppBinding
import com.ingencode.reciclaia.ui.components.FragmentBase
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.AndroidEntryPoint

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-12.
 */

@AndroidEntryPoint
class FragmentApp : FragmentBase() {
    private lateinit var binding: FragmentAppBinding

    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass

    override fun initProperties() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.appNavFragment) as NavHostFragment
        binding.bnvApp.setupWithNavController(navHostFragment.navController)
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentAppBinding.inflate(layoutInflater)
        return binding
    }

}