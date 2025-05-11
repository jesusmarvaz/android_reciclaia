package com.ingencode.reciclaia.ui.screens.app.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ingencode.reciclaia.data.repositories.IClassificationRepository
import com.ingencode.reciclaia.domain.model.ClassificationModel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.BackgroundDispatcher.Background
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-10.
 */

@HiltViewModel
class HistoryViewmodel @Inject constructor(private val databaseProvider: IClassificationRepository): ViewModelBase() {
    override fun theTag(): String = this.nameClass

    private val _classifications = MutableLiveData<ArrayList<ClassificationModel>>()
    val classifications: LiveData<ArrayList<ClassificationModel>> = _classifications

    fun getClassifications() {
        viewModelScope.launch(Dispatchers.Background) {
            val list = arrayListOf<ClassificationModel>()
            list.addAll(databaseProvider.getAllProcessedImages())
            _classifications.postValue(list)
        }
    }
}