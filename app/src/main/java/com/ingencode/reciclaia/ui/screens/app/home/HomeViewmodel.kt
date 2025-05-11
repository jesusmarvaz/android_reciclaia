package com.ingencode.reciclaia.ui.screens.app.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ingencode.reciclaia.data.repositories.IClassificationRepository
import com.ingencode.reciclaia.domain.model.ClassificationModel
import com.ingencode.reciclaia.domain.model.HomeClassificationModel
import com.ingencode.reciclaia.domain.model.HomeModel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.BackgroundDispatcher.Background
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-15.
 */
/*@HiltViewModel
class HistoryViewmodel @Inject constructor(private val databaseProvider: IClassificationRepository): ViewModelBase() {
    override fun theTag(): String = this.nameClass

    private var classificationsRaw = listOf<ClassificationModel>()
        set(value) {
            val listOrdered = orderClassifications(classifications = value, criterion = _radioId.value,
                isDescending = _descendingChecked.value == true)
            _classificationsOrdered.postValue(ArrayList(listOrdered))
            field = value
        }*/
@HiltViewModel
class HomeViewmodel @Inject constructor(private val databaseProvider: IClassificationRepository): ViewModelBase() {
    override fun theTag(): String = this.nameClass

    private val _homeModel = MutableLiveData<HomeModel>()
    val homeModel: LiveData<HomeModel> = _homeModel

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
}