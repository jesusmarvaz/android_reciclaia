package com.ingencode.reciclaia.ui.screens.app.home

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.databinding.FragmentHomeBinding
import com.ingencode.reciclaia.domain.model.HomeClassificationModel
import com.ingencode.reciclaia.domain.model.HomeTop3
import com.ingencode.reciclaia.ui.components.FragmentBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.ui.screens.app.home.adapters.ClassificationsHomeAdapter
import com.ingencode.reciclaia.ui.viewmodels.ImageLauncherViewModel
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.AndroidEntryPoint

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

@AndroidEntryPoint
class FragmentHome : FragmentBaseForViewmodel() {
    override fun getPb(): ProgressBar? = binding.pbHorizontal.progressBarBase
    override fun getShaderLoading(): View? = null
    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner
    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass
    override fun getViewModelBase(): ViewModelBase? = viewmodel

    override fun onResume() {
        super.onResume()
        viewmodel.getClassifications()
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var classificationsHomeAdapter: ClassificationsHomeAdapter
    private val viewmodel: HomeViewmodel by viewModels()
    private val sharedViewmodel: ImageLauncherViewModel by activityViewModels()
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun observeVM() {
        viewmodel.homeModel.observe(viewLifecycleOwner) {
            model ->
            if (model == null) setEmpty()
            else {
                binding.myScrollView.visibility = View.VISIBLE
                binding.noCaptionsLayout.visibility = View.GONE
                configureRV(model.listLast10)
                setTotalCaptions(model.total)
                setAverageConfidence(model.averageConfidence)
                setTop3(model.fixedArrayTop3)
            }
        }
    }

    private fun setEmpty() {
        binding.myScrollView.visibility = View.GONE
        binding.noCaptionsLayout.visibility = View.VISIBLE
    }


    override fun initProperties() {
        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        classificationsHomeAdapter = ClassificationsHomeAdapter(requireContext(), sharedViewmodel)
        configureRV(arrayListOf())
        binding.title.tvScreenTitle.text = getString(R.string.home)
        observeVM()
        viewmodel.getClassifications()
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding
    }

    private fun configureRV(list: List<HomeClassificationModel>) {
        binding.rvLastCaptures.layoutManager = linearLayoutManager
        binding.rvLastCaptures.adapter = classificationsHomeAdapter
        classificationsHomeAdapter.addAll(list)
    }

    private fun setTotalCaptions(total: Int) {
        binding.tvAmountCapturesValue.text = total.toString()
    }

    private fun setAverageConfidence(averageConfidence: Float) {
        val confidence = (averageConfidence * 100).toInt()
        binding.tvConfidenceValue.text = "%d%%".format(confidence)
        binding.pbCircularConfidence.progress = confidence
    }

    private fun setTop3(top3list: Array<HomeTop3?>) {
        val top1 = top3list[0]
        val top2 = top3list[1]
        val top3 = top3list[2]

        if (top1 == null && top2 == null && top3 == null) {
            binding.tvTop3Categories.visibility = View.GONE
            binding.llTop3.visibility = View.GONE
            return
        }
        binding.tvTop3Categories.visibility = View.VISIBLE
        binding.llTop3.visibility = View.VISIBLE

        binding.top3Gold.tvTagTop1.isSelected = true
        binding.top3Silver.tvTagTop2.isSelected = true
        binding.top3Copper.tvTagTop3.isSelected = true
        top1?.let {
            binding.top3Gold.tvTagAmount1.text = it.amount
            binding.top3Gold.tvTagTop1.text = getString(it.wasteTag.idStringName)
        }
        top2?.let {
            binding.top3Silver.tvTagAmount2.text = it.amount
            binding.top3Silver.tvTagTop2.text = getString(it.wasteTag.idStringName)
        }
        top3?.let {
            binding.top3Copper.tvTagAmount3.text = it.amount
            binding.top3Copper.tvTagTop3.text = getString(it.wasteTag.idStringName)
        }
    }
}