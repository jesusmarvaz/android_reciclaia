package com.ingencode.reciclaia.data.repositories

import com.ingencode.reciclaia.domain.model.GlassBottle
import com.ingencode.reciclaia.domain.model.MetalCan
import com.ingencode.reciclaia.domain.model.OrganicWaste
import com.ingencode.reciclaia.domain.model.PlasticBottle
import com.ingencode.reciclaia.domain.model.Waste
import javax.inject.Inject

/**
Created with ❤ by jesusmarvaz on 2025-05-05.
 */

interface IWasteRepository {
    val waste: HashMap<String, Waste>
    fun findMostProbableWaste(search: String): Waste
}

class WasteRepositoryMock @Inject constructor(): IWasteRepository {
    override val waste: HashMap<String, Waste> by lazy { buildWaste() }

    private fun buildWaste(): HashMap<String, Waste> {
        val hashMap: HashMap<String, Waste> = hashMapOf()
        hashMap.put("can", MetalCan())
        hashMap.put("plastic bottle", PlasticBottle())
        hashMap.put("glass bottle", GlassBottle())
        hashMap.put("banana peel", OrganicWaste())
        return hashMap
    }

    override fun findMostProbableWaste(search: String): Waste {
        TODO("not implemented yet, ups!")
    }
}