package com.ingencode.reciclaia.ui.screens.app

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.databinding.FragmentAppBinding
import com.ingencode.reciclaia.ui.components.FragmentBase
import com.ingencode.reciclaia.ui.components.FragmentBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.ui.screens.initial.FragmentInitialViewModel
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-12.
 */

@AndroidEntryPoint
class FragmentApp : FragmentBase() {
    private lateinit var binding: FragmentAppBinding

    override fun goBack() = requireActivity().finish()

    override fun getFragmentTag(): String = this.nameClass

    override fun initProperties() {
        TODO("Not yet implemented")
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentAppBinding.inflate(layoutInflater)
        return binding
    }

}