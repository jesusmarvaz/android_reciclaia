package com.ingencode.reciclaia.ui.components

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ingencode.reciclaia.utils.ILog
import com.ingencode.reciclaia.utils.ISealedError
import com.ingencode.reciclaia.utils.SealedApiError
import com.ingencode.reciclaia.utils.SealedAppError
import com.ingencode.reciclaia.utils.nameClass

/**
Created with ❤ by jesusmarvaz on 2025-01-12.
*/

abstract class ViewModelBase: ViewModel(), ILog {
    protected val sealedError: MutableLiveData<ISealedError?> = MutableLiveData(null)
    fun observableSealedError(): LiveData<ISealedError?> = sealedError
    protected val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    fun observableLoading(): LiveData<Boolean> = loading

    private fun manageSealedError() {
        when (sealedError.value as SealedAppError) {
            is SealedAppError.DefaultError -> {}
            is SealedAppError.ConnectivityError -> {}
            is SealedAppError.WrongFormData -> {}
            is SealedAppError.ProblemSavingImagesLocally -> {}
            is SealedAppError.LocalRepositoryError -> {}
            is SealedAppError.InferenceError -> { }
        }
        //método de ILog: Evita tener que incluir la etiqueta
        logError("manageSealedError: ${sealedError.value?.nameClass}")
        //Log.e(this.nameClass, "manageSealedError: ${sealedError.value?.nameClass}", )
    }

    private fun manageSealedApiError() {
        //método de ILog: Evita tener que incluir la etiqueta
        logError("manageSealedApiError: ${sealedError.value?.nameClass}")
        //Log.e(this.nameClass, "manageSealedApiError: ${sealedError.value?.nameClass}", )
    }

    open fun onError() {
        //método de ILog: Evita tener que incluir la etiqueta
        logDebug("error: ${sealedError.nameClass}")
        //Log.d(this.nameClass, "error: ${sealedError.nameClass}")
        val error = sealedError.value ?: return
        when (error) {
            is SealedAppError -> manageSealedError()
            is SealedApiError -> manageSealedApiError()
        }
    }
}