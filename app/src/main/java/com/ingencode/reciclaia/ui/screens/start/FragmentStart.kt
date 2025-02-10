package com.ingencode.reciclaia.ui.screens.start

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.databinding.FragmentStartBinding
import com.ingencode.reciclaia.ui.components.FragmentBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.nameClass

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-07.
 */

class FragmentStart: FragmentBaseForViewmodel() {
    private lateinit var binding: FragmentStartBinding

    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass
    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner
    override fun getViewModelBase(): ViewModelBase? = null

    override fun initProperties() {
        binding.tvTitle.setOnClickListener {
            findNavController()
                .navigate(FragmentStartDirections.actionFragmentStartToFragmentInitial())
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