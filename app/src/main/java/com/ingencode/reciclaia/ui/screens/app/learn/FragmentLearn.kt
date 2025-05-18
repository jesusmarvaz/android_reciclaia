package com.ingencode.reciclaia.ui.screens.app.learn

import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.databinding.FragmentLearnBinding
import com.ingencode.reciclaia.ui.components.FragmentBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.ILog
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.AndroidEntryPoint

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

@AndroidEntryPoint
class FragmentLearn : FragmentBaseForViewmodel(), ILog {
    private lateinit var binding: FragmentLearnBinding
    private val viewmodel: LearnViewmodel by viewModels()
    override fun getViewModelBase(): ViewModelBase? = viewmodel
    override fun getPb(): ProgressBar? = binding.pb.progressBarBase
    override fun getShaderLoading(): View? = null
    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner
    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass

    override fun observeVM() {
        viewmodel.learnModel.observe(this) {
            Toast.makeText(requireContext(), it?.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun initProperties() {
        binding.title.tvScreenTitle.text = getString(R.string.profile)
        configureRVs()
        viewmodel.getLearnData()
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentLearnBinding.inflate(layoutInflater)
        return binding
    }

    private fun configureRVs() {
        logDebug("Configurando RVs")
    }
}