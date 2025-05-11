package com.ingencode.reciclaia.ui.screens.app.history

import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.data.repositories.IClassificationRepository
import com.ingencode.reciclaia.domain.model.ClassificationModel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.BackgroundDispatcher.Background
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-10.
 */

@HiltViewModel
class HistoryViewmodel @Inject constructor(private val databaseProvider: IClassificationRepository): ViewModelBase() {
    override fun theTag(): String = this.nameClass

    private var classificationsRaw = listOf<ClassificationModel>()
        set(value) {
            val listOrdered = orderClassifications(classifications = value, criterion = _radioId.value,
                isDescending = _descendingChecked.value == true)
            _classificationsOrdered.postValue(ArrayList(listOrdered))
            field = value
        }
    private val _classificationsOrdered = MutableLiveData<ArrayList<ClassificationModel>>()
    val classificationsOrdered: LiveData<ArrayList<ClassificationModel>> = _classificationsOrdered

    private val _deletedNItems = MutableLiveData<Int>()
    val deletedNItems: LiveData<Int> = _deletedNItems

    private val _justDeleted = MutableLiveData<Unit>()
    val justDeleted: LiveData<Unit> = _justDeleted

    private val _radioId = MutableLiveData<Int>(null)
    val radioId = _radioId as LiveData<Int>

    private val _descendingChecked = MutableLiveData<Boolean>(false)
    val descendingChecked = _descendingChecked as LiveData<Boolean>

    fun setIsDescendingChecked(checked: Boolean) {
        _descendingChecked.value = checked
    }

    fun setRadioId(@IdRes id: Int) {
        _radioId.value = id
    }

    fun getClassifications() {
        viewModelScope.launch(Dispatchers.Background) {
            loading.postValue(true)
            val list = arrayListOf<ClassificationModel>()
            delay(1000)
            databaseProvider.getAllProcessedImages()?.let {
                list.addAll(it)
                list.addAll(orderClassifications(it, _radioId.value, _descendingChecked.value == true))
                loading.postValue(false)
                //_classificationsOrdered.postValue(list)
                classificationsRaw = it
            }
        }
    }

    private fun orderClassifications(classifications: List<ClassificationModel>, criterion: Int?, isDescending: Boolean): List<ClassificationModel> {
        val sortedList = when (criterion) {
            R.id.radio_tag -> {
                if (isDescending) classifications.sortedByDescending { it.classificationData?.topPrediction?.label }
                else classifications.sortedBy { it.classificationData?.topPrediction?.label }
            }
            R.id.radio_timestamp -> {
                if (isDescending) classifications.sortedByDescending { it.classificationData?.timestamp }
                else classifications.sortedBy { it.classificationData?.timestamp }
            }

            R.id.radio_accuracy -> {
                if (isDescending) classifications.sortedByDescending { it.classificationData?.topPrediction?.confidence }
                else classifications.sortedBy { it.classificationData?.topPrediction?.confidence }
            }
            else -> { classifications }
        }
        return sortedList
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.Background) {
            loading.postValue(true)
            delay(1000)
            databaseProvider.deleteAllProcessedImages().let {
                viewModelScope.launch(Dispatchers.Main.immediate) { _deletedNItems.value = it }
                _justDeleted.postValue(Unit)
            }
            loading.postValue(false)
        }
    }
}