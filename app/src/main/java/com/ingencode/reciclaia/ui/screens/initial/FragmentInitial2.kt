package com.ingencode.reciclaia.ui.screens.initial

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.data.remote.api.Apis
import com.ingencode.reciclaia.utils.nameClass
import com.ingencode.reciclaia.databinding.FragmentInitial2Binding
import com.ingencode.reciclaia.ui.components.FragmentBase
import com.ingencode.reciclaia.ui.components.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created with ❤ by Jesús Martín on 2025-01-12
 */
@AndroidEntryPoint
class FragmentInitial2 : FragmentBase() {
    private lateinit var binding: FragmentInitial2Binding
    private val viewModel: FragmentInitialViewModel by viewModels()

    @Inject
    lateinit var apiTestApi: Apis.TestApi

    override fun goBack() {
        findNavController().popBackStack()
    }

    override fun getFragmentTag(): String = this.nameClass

    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner

    override fun getViewModelBase(): ViewModelBase? = viewModel


    override fun initProperties() {
        logDebug("TODO, iniciar propiedades")
        binding.textViewDestino.setOnClickListener {
            goBack()
        }
        binding.buttonTest.setOnClickListener {
            viewModel.getTest()
        }
        binding.buttonTestDb.setOnClickListener {
            viewModel.getTestDb()
        }

        viewModel.observableText.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvResponse.text = it
            }
        }

        viewModel.observableDbTestData.observe(viewLifecycleOwner) {
            if (it != null && it.count() > 0) {
                val text = "Nombre: ${it[0].name}, edad: ${it[0].age}"
                binding.tvResponse.text = text
            }
        }
    }

    override fun observeVM() {
        logDebug("TODO, observeVM")
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentInitial2Binding.inflate(layoutInflater)
        return binding
    }

    override fun getPb(): ProgressBar? {
        logDebug("TODO, getPb")
        return null
    }

    override fun getShaderLoading(): View? {
        logDebug("TODO, getPb")
        return null
    }
}