package com.ingencode.reciclaia.ui.screens.app

import android.content.Context
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.databinding.FragmentAppBinding
import com.ingencode.reciclaia.ui.components.FragmentBase
import com.ingencode.reciclaia.utils.Constants
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.AndroidEntryPoint

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-12.
 */

@AndroidEntryPoint
class FragmentApp : FragmentBase() {
    private lateinit var binding: FragmentAppBinding
    private var skipTutorial: Boolean = false

    override fun goBack() = requireActivity().finish()

    override fun getFragmentTag(): String = this.nameClass

    override fun initProperties() {
        val sp = requireContext().getSharedPreferences(Constants.SharedPreferencesKeys.sharedPreferencesKey,
            Context.MODE_PRIVATE)
        skipTutorial = sp
            .getBoolean(Constants.SharedPreferencesKeys.skipTutorialKey, false)
        with(binding.checkboxNoTutorial) {
            isChecked = skipTutorial
            setOnCheckedChangeListener {_, isChecked ->
                sp.edit().putBoolean(Constants.SharedPreferencesKeys.skipTutorialKey, isChecked).apply()
            }
        }
        binding.checkboxNoTutorial.isChecked = skipTutorial
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentAppBinding.inflate(layoutInflater)
        return binding
    }

}