package com.ingencode.reciclaia.ui.screens.start

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.databinding.FragmentStartBinding
import com.ingencode.reciclaia.ui.components.FragmentBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.ui.compose.MyComposeWrapper
import com.ingencode.reciclaia.ui.screens.tutorial.Tutorial
import com.ingencode.reciclaia.utils.Constants
import com.ingencode.reciclaia.utils.nameClass

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-07.
 */

class FragmentStart: FragmentBaseForViewmodel() {
    private lateinit var binding: FragmentStartBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var skipTutorial: Boolean = false

    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass
    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner
    override fun getViewModelBase(): ViewModelBase? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val keys = Constants.SharedPreferencesKeys
        sharedPreferences = requireContext().getSharedPreferences(keys.sharedPreferencesKey, Context.MODE_PRIVATE)
        skipTutorial = sharedPreferences.getBoolean(keys.skipTutorialKey, false)
        if (skipTutorial) startApp()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun startApp() {
        val viewVersion = sharedPreferences.getBoolean(Constants.SharedPreferencesKeys.viewVersion, true)
        if (viewVersion) startViewVersionApp() else startComposeVersionApp()
    }

    private fun startViewVersionApp() {
        sharedPreferences.edit()
            .putBoolean(Constants.SharedPreferencesKeys.viewVersion, true).apply()
        findNavController().navigate(FragmentStartDirections.actionFragmentStartToFragmentApp())
    }

    private fun startComposeVersionApp() {
        sharedPreferences.edit()
            .putBoolean(Constants.SharedPreferencesKeys.viewVersion, false).apply()
        findNavController().navigate(FragmentStartDirections.actionFragmentStartToFragmentApp())
    }

    override fun initProperties() {
        val navController = findNavController()
        binding.tvTitle.setOnClickListener {
            navController
                .navigate(FragmentStartDirections.actionFragmentStartToFragmentInitial())
        }

        binding.btTestApi.setOnClickListener {
            navController
                .navigate(FragmentStartDirections.actionFragmentStartToFragmentInitial2())
        }
        binding.btPalette.setOnClickListener {
            navController
                .navigate(FragmentStartDirections.actionFragmentStartToKeyColorsFragment())
        }

        binding.checkboxNoTutorial.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit()
                .putBoolean(Constants.SharedPreferencesKeys.skipTutorialKey, isChecked)
                .apply()
        }
        binding.checkboxNoTutorial.isChecked = skipTutorial
        binding.btAppViewVersion.setOnClickListener { startViewVersionApp() }
        binding.btAppComposeVersion.setOnClickListener { startComposeVersionApp() }

        binding.composeView.setContent {
            MyComposeWrapper {
                Tutorial()
            }
        }
    }

    override fun observeVM() {}

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentStartBinding.inflate(layoutInflater)
        return binding
    }

    override fun getPb(): ProgressBar? = null
    override fun getShaderLoading(): View? = null
}

