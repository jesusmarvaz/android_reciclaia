package com.ingencode.reciclaia.ui.screens.app.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ingencode.reciclaia.data.repositories.IClassificationRepository
import com.ingencode.reciclaia.domain.model.ClassificationModel
import com.ingencode.reciclaia.domain.model.HomeClassificationModel
import com.ingencode.reciclaia.domain.model.HomeModel
import com.ingencode.reciclaia.domain.model.HomeTop3
import com.ingencode.reciclaia.domain.model.Tag
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.BackgroundDispatcher.Background
import com.ingencode.reciclaia.utils.nameClass
import com.ingencode.reciclaia.utils.sha256
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

    private val _homeModel = MutableLiveData<HomeModel?>()
    val homeModel: LiveData<HomeModel?> = _homeModel

    fun getClassifications() {
        viewModelScope.launch(Dispatchers.Background) {
            loading.postValue(true)
            delay(1500)
            databaseProvider.getAllProcessedImages()?.let {
                _homeModel.postValue(mapToHomeModel(it))
                loading.postValue(false)
            }
        }
    }

    private fun mapToHomeModel(listClassificationModel: List<ClassificationModel>): HomeModel? {
        if (listClassificationModel.isEmpty()) return null
        val size = if (listClassificationModel.size > 9) 9 else listClassificationModel.size
        val listLast10 = listClassificationModel.sortedByDescending { it.classificationData?.timestamp }
            .subList(0, size)
            .map { HomeClassificationModel(
                uri = it.uri,
                id = it.toString().sha256(),
                tag = it.classificationData?.topPrediction?.label ?: "",
                confidence = it.classificationData?.topPrediction?.confidence ?: 0f)
            }
        var sum = 0f
        for (i in 0..listClassificationModel.size - 1) {
            sum += listClassificationModel[i].classificationData?.topPrediction?.confidence ?: 0f
        }
        val averageConfidence = sum / listClassificationModel.size

        val tags = arrayListOf<Tag>()
        Tag.entries.forEach { tag ->
            listClassificationModel.forEach { model ->
                if (model.getTag()?.equals(tag) == true) {
                    tags.add(tag)
                }
            }
        }

        // Count distinct elements and get the top 3 by amount
        val tagCounts = tags.groupingBy { it }.eachCount()
        val sortedTop3 = tagCounts.entries
            .sortedByDescending { it.value }
            .take(3)
            .map { entry ->
                HomeTop3(
                    wasteTag = entry.key,
                    amount = entry.value.toString())
            }

        val top1: HomeTop3? = if (sortedTop3.isNotEmpty()) sortedTop3[0] else null
        val top2: HomeTop3? = if (sortedTop3.size > 1) sortedTop3[1] else null
        val top3: HomeTop3? = if (sortedTop3.size > 2) sortedTop3[2] else null
        val top3Array = arrayOf(top1, top2, top3)

        return HomeModel(listLast10 = ArrayList(listLast10),
            averageConfidence = averageConfidence,
            total = listClassificationModel.size,
            fixedArrayTop3 = top3Array)
    }
}