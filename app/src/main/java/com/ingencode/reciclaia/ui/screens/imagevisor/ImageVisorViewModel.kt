package com.ingencode.reciclaia.ui.screens.imagevisor

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ingencode.reciclaia.data.remote.api.SealedResult
import com.ingencode.reciclaia.data.repositories.IProcessedImageModelRepository
import com.ingencode.reciclaia.data.repositories.LocalStorageProvider
import com.ingencode.reciclaia.domain.model.ProcessedImageModel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.SealedAppError
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
Created with ❤ by jesusmarvaz on 2025-04-11.
 */


@HiltViewModel
class ImageVisorViewModel @Inject constructor(private val localStorageProvider: LocalStorageProvider, private val localDataBaseProvider: IProcessedImageModelRepository) : ViewModelBase() {
    private val _exportedSuccessfully: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val exportedSuccessfully: LiveData<Boolean> = _exportedSuccessfully
    private val _uri: MutableLiveData<Uri> = MutableLiveData<Uri>()
    val uri: LiveData<Uri> = _uri
    private val _processedImageResult: MutableLiveData<SealedResult<ProcessedImageModel>> = MutableLiveData<SealedResult<ProcessedImageModel>>()
    val processedImageResult: LiveData<SealedResult<ProcessedImageModel>> = _processedImageResult

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
                    val processedImage = ProcessedImageModel.Builder(uri).build()
                    _processedImageResult.postValue(SealedResult.ResultSuccess<ProcessedImageModel>(processedImage))
                } else {
                    _processedImageResult.postValue(SealedResult.ResultError(SealedAppError.ProblemSavingImagesLocally()))
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
                val result = localStorageProvider.exportBitmapToNewFileInMediaStore(bitmap)
                if (result is SealedResult.ResultSuccess<Uri>) {
                    _exportedSuccessfully.postValue(true)
                } else if (result is SealedResult.ResultError){
                    sealedError.postValue(result.error)
                }
                loading.postValue(false)
            }
        }
    }

    override fun theTag(): String = nameClass
}