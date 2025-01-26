package com.ingencode.reciclaia.ui.screens.initial

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.common.nameClass
import com.ingencode.reciclaia.databinding.FragmentInitial2Binding
import com.ingencode.reciclaia.ui.common.FragmentBase
import com.ingencode.reciclaia.ui.common.ViewModelBase

/**
 * Created with ❤ by Jesús Martín on 2025-01-12
 */

class FragmentInitial2 : FragmentBase() {
    private lateinit var binding: FragmentInitial2Binding

    override fun goBack() {
        logDebug("TODO, goBack")
        findNavController().popBackStack()
    }

    override fun getFragmentTag(): String = this.nameClass

    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner

    override fun getViewModelBase(): ViewModelBase? = null


    override fun initProperties() {
        logDebug("TODO, iniciar propiedades")
        binding.textViewDestino.setOnClickListener {
            goBack()
        }
    }

    override fun observeVM() {
        logDebug("TODO, observeVM")
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentInitial2Binding.inflate(layoutInflater)
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
}