package com.ingencode.reciclaia.ui.screens.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.databinding.FragmentAppComposeBinding
import com.ingencode.reciclaia.ui.components.FragmentBase
import com.ingencode.reciclaia.ui.navigation.compose.MyBottomNavigationBar
import com.ingencode.reciclaia.ui.navigation.compose.NavigationForBottomBar
import com.ingencode.reciclaia.ui.theme.MyComposeWrapper
import com.ingencode.reciclaia.utils.nameClass

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

class FragmentAppComposeVersion : FragmentBase() {
    private lateinit var binding: FragmentAppComposeBinding

    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass

    override fun initProperties() {
        binding.appComposeVersion.setContent { MyComposeWrapper { AppInCompose() } }
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentAppComposeBinding.inflate(layoutInflater)
        return binding
    }

    @Composable
    fun AppInCompose() {
        val navController = rememberNavController()
        Scaffold(
            topBar = {Text(LocalContext.current.getString(R.string.compose_based_app), style = MaterialTheme.typography.bodySmall, modifier = Modifier.background(
                MaterialTheme.colorScheme.surfaceVariant).fillMaxWidth())},
            bottomBar = { MyBottomNavigationBar(navController) }) {
            padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                NavigationForBottomBar(navController = navController)
            }
        }
    }
}