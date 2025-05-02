package com.ingencode.reciclaia.ui.screens.imagevisor

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.data.remote.api.SealedResult
import com.ingencode.reciclaia.data.repositories.IAProviderInterface
import com.ingencode.reciclaia.data.repositories.IClassificationRepository
import com.ingencode.reciclaia.data.repositories.LocalStorageProvider
import com.ingencode.reciclaia.domain.model.ClassificationModel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.SealedAppError
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val _exportedSuccessfully: MutableLiveData<Unit> = MutableLiveData<Unit>()
    val exportedSuccessfully: LiveData<Unit> = _exportedSuccessfully
    private val _dataSavedSuccessfully: MutableLiveData<Unit> = MutableLiveData<Unit>()
    val dataSavedSuccessfully: LiveData<Unit> = _dataSavedSuccessfully
    private val _classificationResult: MutableLiveData<SealedResult<ClassificationModel>> =
        MutableLiveData<SealedResult<ClassificationModel>>()
    val classificationResult: LiveData<SealedResult<ClassificationModel>> = _classificationResult

    fun setUri(uri: Uri) {
        if(getClassificationFromResult(_classificationResult.value)?.uri == null) {
            val model = ClassificationModel(uri = uri)
            _classificationResult.postValue(SealedResult.ResultSuccess<ClassificationModel>(model))
        }
    }

    fun getClassificationBackgroundColor(context: Context): Int? {
        val topPrediction = getClassificationFromResult(_classificationResult.value)?.topPrediction
        if (topPrediction == null) return null
        val c = topPrediction.confidence
        val backgroundColor =  when {
            (c < 0.25 && c >= 0) -> { R.color.confidence_25_background }
            (c >= 0.25 && c < 0.5) -> { R.color.confidence_50_background }
            (c >= 0.5 && c < 0.7) -> { R.color.confidence_70_background }
            (c >= 0.7 && c < 0.85) -> { R.color.confidence_85_background }
            (c >= 0.85 && c <= 1 ) -> { R.color.confidence_100_background }
            else -> null
        }
        if (backgroundColor == null) return null
        return ContextCompat.getColor(context, backgroundColor)
    }

    fun getClassificationTextColor(context: Context): Int? {
        val topPrediction = getClassificationFromResult(_classificationResult.value)?.topPrediction
        if (topPrediction == null) return null
        val c = topPrediction.confidence
        val textColor =  when {
            (c < 0.25 && c >= 0) -> { R.color.confidence_25 }
            (c >= 0.25 && c < 0.5) -> { R.color.confidence_50 }
            (c >= 0.5 && c < 0.7) -> { R.color.confidence_70 }
            (c >= 0.7 && c < 0.85) -> { R.color.confidence_85 }
            (c >= 0.85 && c <= 1 ) -> { R.color.confidence_100 }
            else -> null
        }
        if (textColor == null) return null
        return ContextCompat.getColor(context, textColor)
    }

    fun processButtonPressed() {
        loading.postValue(true)
        viewModelScope.launch {
            getUriFromResult(_classificationResult.value)?.let {
                val result = classificationProvider.getClassificationFromInference(it)
                _classificationResult.postValue(SealedResult.ResultSuccess<ClassificationModel>(result))
            }
            loading.postValue(false)
        }
    }

    fun getClassificationFromResult(result: SealedResult<ClassificationModel>?): ClassificationModel? {
        return (result as? SealedResult.ResultSuccess<ClassificationModel>)?.data
    }

    fun getUriFromResult(result: SealedResult<ClassificationModel>?): Uri? = getClassificationFromResult(result)?.uri


    fun savedButtonPressed(bitmap: Bitmap, title: String? = null, comments: String? = null) {
        val classification = getClassificationFromResult(_classificationResult.value)
        val uri: Uri? = classification?.uri
        uri?.let {
            loading.postValue(true)
            viewModelScope.launch(Dispatchers.IO) {
                val uri = localStorageProvider.saveCroppedImage(bitmap, it)
                if (uri != null) {
                    delay(1000)
                    if (classification.predictions.isEmpty())
                    {
                        val classification = ClassificationModel(uri = uri, title = title, comments = comments)
                        _classificationResult.postValue(SealedResult.ResultSuccess<ClassificationModel>(classification))
                    } else {
                        getClassificationFromResult(_classificationResult.value)?.also {
                            it.uri = uri
                            it.title = title
                            it.comments = comments
                        }
                    }

                    localDataBaseProvider.updateProcessedImage(getClassificationFromResult(_classificationResult.value)!!)
                    _dataSavedSuccessfully.postValue(Unit)
                } else {
                    _classificationResult.postValue(SealedResult.ResultError(SealedAppError.ProblemSavingImagesLocally()))
                }
                loading.postValue(false)
            }
        }
    }

    fun shareToLocalMediaFolderButtonPressed(bitmap: Bitmap) {
        loading.postValue(true)
        viewModelScope.launch {
            delay(1000)
            val result = localStorageProvider.exportBitmapToNewFileInMediaStore(bitmap)
            if (result is SealedResult.ResultSuccess<Uri>) {
                _exportedSuccessfully.postValue(Unit)
            } else if (result is SealedResult.ResultError) {
                sealedError.postValue(result.error)
            }
            loading.postValue(false)
        }
    }

    override fun theTag(): String = nameClass
}