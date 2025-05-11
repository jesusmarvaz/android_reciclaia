package com.ingencode.reciclaia.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-10.
 */
abstract class FragmentBaseForViewmodel: FragmentBase(), IViewBase {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflatedViewBinding().root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)
        // (activity as AppCompatActivity).setLightStatusBar()
        initProperties()
        observeViewModelBase()
        observeVM()
    }
}