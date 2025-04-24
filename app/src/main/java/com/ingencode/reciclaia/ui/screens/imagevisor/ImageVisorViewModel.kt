package com.ingencode.reciclaia.ui.screens.imagevisor

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ingencode.reciclaia.data.remote.api.SealedResult
import com.ingencode.reciclaia.data.repositories.IAProviderInterface
import com.ingencode.reciclaia.data.repositories.IClassificationRepository
import com.ingencode.reciclaia.data.repositories.LocalStorageProvider
import com.ingencode.reciclaia.domain.model.ClassificationModel
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
class ImageVisorViewModel @Inject constructor(
    private val localStorageProvider: LocalStorageProvider,
    private val localDataBaseProvider: IClassificationRepository,
    private val classificationProvider: IAProviderInterface
) : ViewModelBase() {
    private val _exportedSuccessfully: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val exportedSuccessfully: LiveData<Boolean> = _exportedSuccessfully
    private val _classificationResult: MutableLiveData<SealedResult<ClassificationModel>> =
        MutableLiveData<SealedResult<ClassificationModel>>()
    val classificationResult: LiveData<SealedResult<ClassificationModel>> = _classificationResult

    fun setUri(uri: Uri) {
        val model = ClassificationModel.Builder(uri).build()
        _classificationResult.postValue(SealedResult.ResultSuccess<ClassificationModel>(model))
    }

    fun getUriFromResult(): Uri? =
        (_classificationResult.value as? SealedResult.ResultSuccess<ClassificationModel>)?.data?.uri

    fun savedButtonPressed(bitmap: Bitmap) {
        val uri: Uri? = getUriFromResult()
        uri?.let {
            loading.postValue(true)
            viewModelScope.launch {
                val uri = localStorageProvider.saveCroppedImage(bitmap, it)
                if (uri != null) {
                    delay(1000)
                    val processedImage = ClassificationModel.Builder(uri).build()
                    _classificationResult.postValue(
                        SealedResult.ResultSuccess<ClassificationModel>(
                            processedImage
                        )
                    )
                } else {
                    _classificationResult.postValue(SealedResult.ResultError(SealedAppError.ProblemSavingImagesLocally()))
                }
                loading.postValue(false)
            }
        }
    }

    fun classifyButtonPressed(bitmap: Bitmap) {
        viewModelScope.launch {
            val classification = classificationProvider.getClassificationFromInference()
            _classificationResult.postValue(
                SealedResult<ClassificationModel>.ResultSuccess(
                    classification
                )
            )
        }
    }

    fun shareToLocalMediaFolderButtonPressed(bitmap: Bitmap) {
        loading.postValue(true)
        viewModelScope.launch {
            delay(1000)
            val result = localStorageProvider.exportBitmapToNewFileInMediaStore(bitmap)
            if (result is SealedResult.ResultSuccess<Uri>) {
                _exportedSuccessfully.postValue(true)
            } else if (result is SealedResult.ResultError) {
                sealedError.postValue(result.error)
            }
            loading.postValue(false)
        }
    }

    override fun theTag(): String = nameClass
}