package com.ingencode.reciclaia.ui.screens.app.learn

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.databinding.FragmentLearnBinding
import com.ingencode.reciclaia.domain.model.LearnModelBundle
import com.ingencode.reciclaia.ui.components.FragmentBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.ILog
import com.ingencode.reciclaia.utils.SealedApiError
import com.ingencode.reciclaia.utils.SealedAppError
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.AndroidEntryPoint

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

@AndroidEntryPoint
class FragmentLearn : FragmentBaseForViewmodel(), ILog {
    private lateinit var binding: FragmentLearnBinding
    private val viewmodel: LearnViewmodel by viewModels()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var learnAdapter: LearnAdapter

    override fun getViewModelBase(): ViewModelBase? = viewmodel
    override fun getPb(): ProgressBar? = binding.pb.progressBarBase
    override fun getShaderLoading(): View? = null
    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner
    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass

    override fun observeVM() {
        viewmodel.observableSealedError().observe(this) {
            if (it != null) {
                binding.tvError.text =
                    when (it) {
                        is SealedAppError.ConnectivityError -> {"Error de conexión: Comprueba tu conexión a internet: ${it.message}"}
                        is SealedAppError -> {"App error: ${it.message}"}
                        is SealedApiError -> {"Server error${it.message}"}
                    }
                binding.cvError.visibility = View.VISIBLE
                binding.rvLearn.visibility = View.GONE
            }
        }
        viewmodel.learnModel.observe(this) {
            if (it != null) { configureRVs(it) }
        }
    }

    override fun initProperties() {
        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.title.tvScreenTitle.text = getString(R.string.learn)
        binding.btRefresh.setOnClickListener { viewmodel.getLearnData() }
        viewmodel.getLearnData()
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentLearnBinding.inflate(layoutInflater)
        return binding
    }

    private fun configureRVs(list: List<LearnModelBundle>) {
        binding.rvLearn.visibility = View.VISIBLE
        binding.cvError.visibility = View.GONE
        learnAdapter = LearnAdapter(ArrayList(list))
        binding.rvLearn.layoutManager = linearLayoutManager
        binding.rvLearn.adapter = learnAdapter
    }
}