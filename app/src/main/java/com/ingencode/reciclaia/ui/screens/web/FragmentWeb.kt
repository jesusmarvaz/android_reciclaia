package com.ingencode.reciclaia.ui.screens.web

import android.webkit.WebViewClient
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.databinding.FragmentWebBinding
import com.ingencode.reciclaia.ui.components.FragmentBase
import com.ingencode.reciclaia.ui.navigation.NavHostFragments
import com.ingencode.reciclaia.utils.getSerializable
import com.ingencode.reciclaia.utils.nameClass

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-24.
 */
class FragmentWeb: FragmentBase() {
    override fun goBack() { findNavController().popBackStack() }
    override fun getFragmentTag(): String = this.nameClass
    private var navController: NavController? = null
    private lateinit var b: FragmentWebBinding
    private lateinit var title: String
    private lateinit var argumentUrl: String


    override fun initProperties() {
        title = arguments?.getString("title", "") ?: ""
        argumentUrl = arguments?.getString("url", "") ?: ""
        val enum = getSerializable<NavHostFragments>("enumnavhostfragment")
        navController = enum?.let { requireActivity().findNavController(it.idNavHostFragment) }
        b.tvTitle.text = title
        b.webDisplay.apply {
            //settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(argumentUrl)
        }
        b.back.setOnClickListener { goBack() }
    }

    override fun getInflatedViewBinding(): ViewBinding {
        b = FragmentWebBinding.inflate(layoutInflater)
        return b
    }
}