package com.ingencode.reciclaia.ui.screens.app.learn

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ingencode.reciclaia.data.remote.api.Apis
import com.ingencode.reciclaia.data.remote.mappers.mapToModel
import com.ingencode.reciclaia.domain.model.LearnModelBundle
import com.ingencode.reciclaia.domain.model.ProcessingType
import com.ingencode.reciclaia.domain.model.UrlModel
import com.ingencode.reciclaia.domain.model.WasteType
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.BackgroundDispatcher.Background
import com.ingencode.reciclaia.utils.SealedApiError
import com.ingencode.reciclaia.utils.SealedAppError
import com.ingencode.reciclaia.utils.isAnyNetworkActive
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-18.
 */
@HiltViewModel
class LearnViewmodel @Inject constructor(@ApplicationContext val c: Context, private val apiProvider: Apis.WasteInfoApi) : ViewModelBase() {
    private val titles: List<String> = arrayListOf("Tipos de residuos", "Tipos de procesado", "Enlaces para ampliar información")

    override fun theTag(): String = this.nameClass
    private val _learnModel = MutableLiveData<List<LearnModelBundle>>()
    val learnModel: LiveData<List<LearnModelBundle>> = _learnModel

    fun getLearnData() {
        viewModelScope.launch {
            loading.postValue(true)
            val deferredJobWasteTypes = async(Dispatchers.Background) { getWasteTypes() }
            val deferredJobProcessingTypes = async(Dispatchers.Background) { getProcessingTypes() }
            val deferredUrls = async(Dispatchers.Background) { getUrls() }

            /*val results = awaitAll(deferredJobWasteTypes, deferredJobProcessingTypes, deferredUrls)
            if (results.any(){it == null} || results.size < 3) {
                sealedError.postValue(SealedApiError.ServerError(null, null))
                return@launch
            }
            val listWasteTypes:List<WasteType> = results[0] as List<WasteType>
            val listProcessingTypes = results[1] as List<*>
            val listUrls = results[2] as List<*>
            _learnModel.postValue(LearnModel(
                wasteTypes = listWasteTypes,
                processingTypes = results[1],
                urls = results[2]))*/

            val listWasteTypesResult: List<WasteType>? = deferredJobWasteTypes.await()
            val listProcessingTypesResult: List<ProcessingType>? = deferredJobProcessingTypes.await()
            val listUrlsResult: List<UrlModel>? = deferredUrls.await()

            val lists = listOf(listUrlsResult, listProcessingTypesResult, listWasteTypesResult)
            if (lists.any { it == null} ){
                sealedError.postValue(SealedApiError.ServerError(null, null))
                loading.postValue(false)
                return@launch
            }
            val list = arrayListOf<LearnModelBundle>()

            list.add(LearnModelBundle.TitleItem(titles[0]))
            list.addAll(listWasteTypesResult!!.map { LearnModelBundle.WasteItem(it) })
            list.add(LearnModelBundle.TitleItem(titles[1]))
            list.addAll(listProcessingTypesResult!!.map { LearnModelBundle.ProcessingItem(it)} )
            list.add(LearnModelBundle.TitleItem(titles[2]))
            list.addAll((listUrlsResult!!.map { LearnModelBundle.UrlItem(it)}))

            _learnModel.postValue(list)
            loading.postValue(false)
        }
    }

    private suspend fun getUrls(): List<UrlModel>? {
        return try {
            val list = arrayListOf<UrlModel>()
            val response = apiProvider.getUrls()
            response.body()?.let { list.addAll(it.map { it.mapToModel() }) }
            list
        } catch (e: Exception) {
            val error = if (!c.isAnyNetworkActive()) {
                SealedAppError.ConnectivityError()
            } else {
                SealedAppError.DefaultError("Ups! Ha habido algún error... ${e.message}")
            }
            sealedError.postValue(error)
            logError(e.stackTrace.toString())
            e.printStackTrace()
            null
        }
    }

    private suspend fun getWasteTypes(): List<WasteType>? {
        return try {
            val list = arrayListOf<WasteType>()
            val response = apiProvider.getWasteTypes()
            response.body()?.let { list.addAll(it.map { it.mapToModel() }) }
            list
        } catch (e: Exception) {
            val error = if (!c.isAnyNetworkActive()) {
                SealedAppError.ConnectivityError()
            } else {
                SealedAppError.DefaultError("Ups! Ha habido algún error... ${e.message}")
            }
            sealedError.postValue(error)
            logError(e.stackTrace.toString())
            e.printStackTrace()
            null
        }
    }

    private suspend fun getProcessingTypes(): List<ProcessingType>? {
        return try {
            val list = arrayListOf<ProcessingType>()
            val response = apiProvider.getProcessingTypes()
            response.body()?.let { list.addAll(it.map { it.mapToModel() }) }
            list
        } catch (e: Exception) {
            val error = if (!c.isAnyNetworkActive()) {
                SealedAppError.ConnectivityError()
            } else {
                SealedAppError.DefaultError("Ups! Ha habido algún error... ${e.message}")
            }
            sealedError.postValue(error)
            logError(e.stackTrace.toString())
            e.printStackTrace()
            null
        }
    }
}