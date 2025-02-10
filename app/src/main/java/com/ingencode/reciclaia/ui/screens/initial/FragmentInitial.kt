package com.ingencode.reciclaia.ui.screens.initial

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.utils.nameClass
import com.ingencode.reciclaia.databinding.FragmentInitialBinding
import com.ingencode.reciclaia.ui.components.FragmentBase
import com.ingencode.reciclaia.ui.components.ViewModelBase

/**
 * Created with ❤ by Jesús Martín on 2025-01-12
 */

class FragmentInitial : FragmentBase() {
    override fun getFragmentTag(): String = this.nameClass

    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner

    override fun getViewModelBase(): ViewModelBase? = null
    private lateinit var binding: FragmentInitialBinding

    override fun initProperties() {
        logDebug("TODO, iniciar propiedades")
        binding.textViewOrigen.setOnClickListener {
            findNavController()
                .navigate(FragmentInitialDirections.actionFragmentInitialToFragmentInitial2())
        }
    }

    override fun observeVM() {
        logDebug("TODO, observeVM")
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentInitialBinding.inflate(layoutInflater)
        return binding
    }

    override fun getPb(): ProgressBar? {
        logDebug("TODO, getPb")
        return null
    }

    override fun getShaderLoading(): View? {
        logDebug("TODO, getPb")
        return null
    }

    override fun goBack() {
        logDebug("TODO, goBack")
    }
}