package com.ingencode.reciclaia.ui.common

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ingencode.reciclaia.common.ILog
import com.ingencode.reciclaia.common.ISealedError
import com.ingencode.reciclaia.common.SealedApiError
import com.ingencode.reciclaia.common.SealedError
import com.ingencode.reciclaia.common.nameClass

/**
Created with ❤ by jesusmarvaz on 2025-01-12.
*/

abstract class ViewModelBase: ViewModel(), ILog {
    protected val sealedError: MutableLiveData<ISealedError?> = MutableLiveData(null)
    fun observableSealedError(): LiveData<ISealedError?> = sealedError
    protected val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    fun observableLoading(): LiveData<Boolean> = loading

    private fun manageSealedError() {
        when (sealedError.value as SealedError) {
            is SealedError.DefaultError -> {}
            is SealedError.ConnectivityError -> {}
            is SealedError.WrongFormData -> {}
        }
        Log.e(this.nameClass, "manageSealedError: ${sealedError.value?.nameClass}", )
    }

    private fun manageSealedApiError() {
        Log.e(this.nameClass, "manageSealedApiError: ${sealedError.value?.nameClass}", )
    }

    open fun onError() {
        Log.d(this.nameClass, "error: ${sealedError.nameClass}")
        val error = sealedError.value ?: return
        when (error) {
            is SealedError -> manageSealedError()
            is SealedApiError -> manageSealedApiError()
        }
    }

    override fun theTag(): String = this.getViewModelImpl()
    abstract fun getViewModelImpl(): String
}