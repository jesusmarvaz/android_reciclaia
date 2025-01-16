package com.ingencode.reciclaia.ui.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.common.ILog
import com.ingencode.reciclaia.common.ISealedError
import com.ingencode.reciclaia.common.SealedError

/**
Created with ❤ by jesusmarvaz on 2025-01-12.
*/

abstract class FragmentBase : Fragment(), ILog {
    abstract fun getFragmentTag(): String
    abstract fun getViewLifeCycleOwner(): LifecycleOwner
    abstract fun getViewModelBase(): ViewModelBase?
    abstract fun initProperties()
    abstract fun observeVM()
    abstract fun getInflatedViewBinding(): ViewBinding
    abstract fun getPb(): ProgressBar?
    abstract fun getShaderLoading(): View?
    abstract fun goBack()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = getInflatedViewBinding().root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun theTag(): String = getFragmentTag()
}