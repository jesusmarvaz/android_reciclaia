package com.ingencode.reciclaia.ui.screens.app.history

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.databinding.FragmentHistoryBinding
import com.ingencode.reciclaia.domain.model.ClassificationModel
import com.ingencode.reciclaia.ui.components.FragmentBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.AndroidEntryPoint

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

@AndroidEntryPoint
class FragmentHistory() : FragmentBaseForViewmodel() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewmodel: HistoryViewmodel by viewModels()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapterHistory: ClassificationListAdapter
    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner
    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass

    override fun getViewModelBase(): ViewModelBase? {
        return viewmodel
    }

    override fun observeVM() {
        viewmodel.classifications.observe(this) { configureRV(it) }
    }

    override fun getPb(): ProgressBar? = binding.pbHorizontal.progressBarBase
    override fun getShaderLoading(): View? = binding.shader

    override fun initProperties() {
        layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        adapterHistory = ClassificationListAdapter(requireContext())
        binding.title.tvScreenTitle.text = getString(R.string.history)
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding
    }

    private fun configureRV(list: List<ClassificationModel>) {
       binding.rvItems.adapter = adapterHistory;
        binding.rvItems.layoutManager = layoutManager;
        adapterHistory.addAll(list)
    }
}