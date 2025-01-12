package com.ingencode.reciclaia.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

/**
Created with ❤ by jesusmarvaz on 2025-01-12.
*/

abstract class FragmentBase : Fragment() {
    abstract fun getViewLifeCycleOwner(): LifecycleOwner
    abstract fun getViewModelBase(): ViewModelBase
    abstract fun initProperties()
    abstract fun observeVM()
    abstract fun getInflatedViewBinding(): ViewBinding
    abstract fun getPb(): ProgressBar?
    abstract fun getShaderLoading(): View?
    abstract fun goBack()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = getInflatedViewBinding().root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as AppCompatActivity).setLightStatusBar()
        initProperties()
        observeVM()
        observeViewModelBase()
    }

    open fun onError(error: SealedError?) {
        val vm = getViewModelBase()
        vm.handleError(requireContext())
    }

    private fun observeViewModelBase() {
        val vm = getViewModelBase()
        val pb = getPb()
        val sh = getShaderLoading()
        val lifecycleOwner = getViewLifeCycleOwner()
        vm.observableBcdError().observe(lifecycleOwner) { if (it != null) onError(it) }
        vm.observableLoading().observe(lifecycleOwner) {
            if (it == false) {
                pb?.visibility = View.INVISIBLE
                sh?.visibility = View.GONE
            }
            else {
                pb?.visibility = View.VISIBLE
                sh?.visibility = View.VISIBLE
            }
        }

        vm.observableUser().observe(lifecycleOwner) {
            if (it != null) vm.saveUserInCache(it)
        }
    }
}