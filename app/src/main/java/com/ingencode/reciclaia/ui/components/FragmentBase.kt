package com.ingencode.reciclaia.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.utils.ILog

/**
Created with ❤ by jesusmarvaz on 2025-01-12.
*/

abstract class FragmentBase : Fragment(), ILog {
    abstract fun goBack()
    abstract fun getFragmentTag(): String
    abstract fun initProperties()
    abstract fun getInflatedViewBinding(): ViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        getInflatedViewBinding().root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // (activity as AppCompatActivity).setLightStatusBar()
        initProperties()
    }

    override fun theTag(): String = getFragmentTag()
}