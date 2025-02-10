package com.ingencode.reciclaia.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleOwner

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-10.
 */
abstract class FragmentBaseForViewmodel: FragmentBase() {
    abstract fun getViewLifeCycleOwner(): LifecycleOwner
    abstract fun getViewModelBase(): ViewModelBase?
    abstract fun observeVM()
    abstract fun getPb(): ProgressBar?
    abstract fun getShaderLoading(): View?

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflatedViewBinding().root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)
        // (activity as AppCompatActivity).setLightStatusBar()
        initProperties()
        observeVM()
        observeViewModelBase()
    }

    private fun observeViewModelBase() {
        val vm = getViewModelBase() ?: return
        val pb = getPb()
        val sh = getShaderLoading()
        val lifecycleOwner = getViewLifeCycleOwner()
        vm.observableSealedError().observe(lifecycleOwner) { if (it != null) vm.onError() }
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

        /*vm.observableUser().observe(lifecycleOwner) {
            if (it != null) vm.saveUserInCache(it)
        }*/
    }
}