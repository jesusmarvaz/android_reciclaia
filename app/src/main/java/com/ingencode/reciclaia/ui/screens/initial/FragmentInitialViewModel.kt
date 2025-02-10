package com.ingencode.reciclaia.ui.screens.initial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ingencode.reciclaia.data.remote.api.Apis
import com.ingencode.reciclaia.data.remote.dto.TestResponseDb
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.ILog
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-06.
 */
@HiltViewModel
class FragmentInitialViewModel @Inject constructor(private val testApiProvider: Apis.TestApi) : ViewModelBase(), ILog {
    private val textLiveData: MutableLiveData<String> = MutableLiveData<String>()
    val observableText: LiveData<String> = textLiveData

    private val dbTestLiveData: MutableLiveData<List<TestResponseDb>> = MutableLiveData<List<TestResponseDb>>()
    val observableDbTestData: LiveData<List<TestResponseDb>> = dbTestLiveData

    fun getTest() {
        viewModelScope.launch {
            try {
                val response = testApiProvider.getTest()
                response.body()?.toString()?.let {
                    textLiveData.postValue(it)
                }
            } catch (e: Exception) {
                logError(e.stackTrace.toString())
                e.printStackTrace()
            }
        }
    }

    fun getTestDb() {
        viewModelScope.launch {
            try {
                val response = testApiProvider.getTestDb()
                response.body()?.let {
                    dbTestLiveData.postValue(it)
                }
            } catch (e: Exception) {
                logError(e.stackTrace.toString())
                e.printStackTrace()
            }
        }
    }
    override fun theTag(): String = this.nameClass
}