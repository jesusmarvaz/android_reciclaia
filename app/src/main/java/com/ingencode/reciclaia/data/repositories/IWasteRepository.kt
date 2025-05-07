package com.ingencode.reciclaia.data.repositories

import com.ingencode.reciclaia.domain.model.WasteTagCategory
import javax.inject.Inject

/**
Created with ❤ by jesusmarvaz on 2025-05-05.
 */

interface IWasteRepository {

    fun findMostProbableWasteCategory(search: String): WasteTagCategory
}

class WasteRepositoryMock @Inject constructor(): IWasteRepository {
    override fun findMostProbableWasteCategory(search: String): WasteTagCategory {
        TODO("not implemented yet, ups!")
    }
}