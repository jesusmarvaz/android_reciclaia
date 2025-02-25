package com.ingencode.reciclaia.ui.screens.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.databinding.FragmentStartBinding
import com.ingencode.reciclaia.ui.components.FragmentBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.ui.screens.tutorial.Tutorial
import com.ingencode.reciclaia.ui.theme.MyComposeWrapper
import com.ingencode.reciclaia.ui.viewmodels.SettingsViewModel
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.AndroidEntryPoint

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-07.
 */

@AndroidEntryPoint
class FragmentStart: FragmentBaseForViewmodel() {
    private lateinit var binding: FragmentStartBinding
    private var skipTutorial: Boolean = false
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass
    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner
    override fun getViewModelBase(): ViewModelBase? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        skipTutorial = settingsViewModel.getSkipTutorial()
        if (skipTutorial) startApp()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun startApp() {
        val viewVersion = settingsViewModel.getIsViewVersion()
        if (viewVersion) startViewVersionApp() else startComposeVersionApp()
    }

    private fun startViewVersionApp() {
        settingsViewModel.setIsViewVersion(true)
        findNavController().navigate(FragmentStartDirections.actionFragmentStartToFragmentApp())
    }

    private fun startComposeVersionApp() {
        settingsViewModel.setIsViewVersion(false)
        findNavController().navigate(FragmentStartDirections.actionFragmentStartToFragmentAppComposeVersion())
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
            settingsViewModel.setSkipTutorial(isChecked)
        }
        binding.btAppViewVersion.setOnClickListener { startViewVersionApp() }
        binding.btAppComposeVersion.setOnClickListener { startComposeVersionApp() }

        binding.composeView.setContent {
            MyComposeWrapper { Tutorial() }
        }
    }

    override fun observeVM() {
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentStartBinding.inflate(layoutInflater)
        return binding
    }

    override fun getPb(): ProgressBar? = null
    override fun getShaderLoading(): View? = null
}

