package com.ingencode.reciclaia.data.repositories

import com.ingencode.reciclaia.domain.model.WasteTagCategory
import javax.inject.Inject

/**
Created with ❤ by jesusmarvaz on 2025-05-05.
 */

interface IWasteRepository {
    fun findCategories(search: String): Set<WasteTagCategory>?
}

class WasteRepository @Inject constructor(): IWasteRepository {
    override fun findCategories(search: String): Set<WasteTagCategory>? {
        return WasteTagCategory.entries
            .filter { tag -> tag.tags.any { it.contains(search, ignoreCase = true) } }
            .toSet()
    }
}