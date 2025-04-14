package com.ingencode.reciclaia.ui.screens.imagevisor

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ingencode.reciclaia.data.remote.api.SealedResult
import com.ingencode.reciclaia.data.repositories.LocalStorageProvider
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.SealedError
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
Created with ❤ by jesusmarvaz on 2025-04-11.
 */


@HiltViewModel
class ImageVisorViewModel @Inject constructor(private val localStorageProvider: LocalStorageProvider) : ViewModelBase() {
    private val _uri: MutableLiveData<Uri> = MutableLiveData<Uri>()
    val uri: LiveData<Uri> = _uri
    private val _result: MutableLiveData<SealedResult<Uri>> = MutableLiveData<SealedResult<Uri>>()
    val result: LiveData<SealedResult<Uri>> = _result

    fun setUri(uri: Uri) {
        this._uri.postValue(uri)
    }

    fun savedButtonPressed(bitmap: Bitmap) {
        uri.value?.let{
            loading.postValue(true)
            viewModelScope.launch {
                val uri = localStorageProvider.saveCroppedImage(bitmap, it)
                if (uri != null) {
                    delay(1000)
                    _uri.postValue(uri)
                    _result.postValue(SealedResult.ResultSuccess<Uri>(uri))
                } else {
                    _result.postValue(SealedResult.ResultError(SealedError.ProblemSavingImagesLocally()))
                }
                loading.postValue(false)
            }
        }
    }

    fun shareToLocalMediaFolderButtonPressed(bitmap: Bitmap) {
        uri.value?.let {
            loading.postValue(true)
            viewModelScope.launch {
                delay(1000)
                val succeeded = localStorageProvider.exportBitmapToNewFileInMediaStore(bitmap)
                _result.postValue(succeeded)
                loading.postValue(false)
            }
        }
    }

    override fun theTag(): String = nameClass
}