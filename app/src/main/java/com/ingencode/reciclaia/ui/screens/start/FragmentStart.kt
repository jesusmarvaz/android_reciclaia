package com.ingencode.reciclaia.ui.screens.start

import android.view.View
import android.widget.ProgressBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.databinding.FragmentStartBinding
import com.ingencode.reciclaia.ui.components.FragmentBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.ui.compose.MyComposeWrapper
import com.ingencode.reciclaia.ui.compose.monospaceFontFamily
import com.ingencode.reciclaia.ui.screens.tutorial.Tutorial
import com.ingencode.reciclaia.utils.nameClass

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-07.
 */

class FragmentStart: FragmentBaseForViewmodel() {
    private lateinit var binding: FragmentStartBinding

    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass
    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner
    override fun getViewModelBase(): ViewModelBase? = null

    override fun initProperties() {
        binding.tvTitle.setOnClickListener {
            findNavController()
                .navigate(FragmentStartDirections.actionFragmentStartToFragmentInitial())
        }

        binding.btTestApi.setOnClickListener {
            findNavController()
                .navigate(FragmentStartDirections.actionFragmentStartToFragmentInitial2())
        }
        //TODO binding.btSkip.setOnClickListener()
        //TODO binding.checkboxNoTutorial.setOnClickListener()

        binding.composeView.setContent {
            MyComposeWrapper {
                Tutorial()
            }
        }
    }

    override fun observeVM() {}

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentStartBinding.inflate(layoutInflater)
        return binding
    }

    override fun getPb(): ProgressBar? = null
    override fun getShaderLoading(): View? = null
}

