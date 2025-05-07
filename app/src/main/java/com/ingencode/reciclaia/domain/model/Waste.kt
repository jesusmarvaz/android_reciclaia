package com.ingencode.reciclaia.domain.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.ingencode.reciclaia.R

/**
Created with ❤ by jesusmarvaz on 2025-05-05.
 */

enum class WasteTag(val tag: String) {
    
}
enum class WasteCategory() { ORGANIC, INORGANIC, DANGEROUS, BULKY, CONSTRUCTION, SANITARY, NOT_DEFINED }
sealed class WasteProcessing (@ColorRes val idColor: Int, @StringRes val nameStringResource: Int) {
    object OrganicRestContainer : WasteProcessing(R.color.organic_container, R.string.organic_container)
    object YellowContainer : WasteProcessing(R.color.yellow_container, R.string.yellow_container)
    object BlueContainer : WasteProcessing(R.color.blue_container, R.string.blue_container)
    object GreenContainer : WasteProcessing(R.color.green_container, R.string.green_container)
    object TextileContainer : WasteProcessing(R.color.textile_container, R.string.textile_container)
    object SigrePoint: WasteProcessing(R.color.sigre_point, R.string.sigre_point)
    object RestContainer: WasteProcessing(R.color.rest_container, R.string.rest_container)
    data class CleanPoint(val description: String? = null) : WasteProcessing(R.color.clean_point, R.string.clean_point)
    data class SpecificManagement(val description: String? = null, val managedBy: String? = null) : WasteProcessing(R.color.specific_management, R.string.specific_management)
    data class HazardousProcessing(val description: String? = null, val managedBy: String? = null) : WasteProcessing(R.color.hazardous_processing, R.string.hazardous_processing)
}

abstract class Waste() {
    abstract val tags: Set<String>
    abstract fun getCategory(): WasteCategory
    abstract fun getProcessing(): Set<WasteProcessing>
}

class UncategorizedWaste(override val tags: Set<String>) : Waste() {
    override fun getCategory(): WasteCategory = WasteCategory.NOT_DEFINED
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.RestContainer)
}

class OrganicWaste(override val tags: Set<String> = set"Resíduos orgnánicos"): Waste() {
    override fun getCategory(): WasteCategory = WasteCategory.ORGANIC
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.OrganicRestContainer)
}

class GenericYellowContainerWaste(override val tags: Set<String>): Waste() {
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.YellowContainer)
    override fun getCategory(): WasteCategory = WasteCategory.INORGANIC
}

class PlasticBottle(override val tags: Set<String> = "botellas de plástico") : Waste() {
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.YellowContainer)
    override fun getCategory(): WasteCategory = WasteCategory.INORGANIC
}

class MetalCan(override val tags: Set<String> = "conservas") : Waste() {
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.YellowContainer)
    override fun getCategory(): WasteCategory = WasteCategory.INORGANIC
}
class GenericGreenContainerWaste(override val tags: Set<String>) : Waste() {
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.GreenContainer)
    override fun getCategory(): WasteCategory = WasteCategory.INORGANIC
}

class GlassBottle(override val tags: Set<String> = "botellas de vidrio") : Waste() {
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.GreenContainer)
    override fun getCategory(): WasteCategory = WasteCategory.INORGANIC
}

class GenericBlueContainerWaste(override val tags: Set<String>) : Waste() {
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.BlueContainer)
    override fun getCategory(): WasteCategory = WasteCategory.INORGANIC
}

class PaperAndCarboard(override val tags: Set<String> = "papel y cartón") : Waste() {
    override fun getCategory(): WasteCategory = WasteCategory.INORGANIC
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.BlueContainer)
}

class Textile(override val tags: Set<String> = "textile"): Waste() {
    override fun getCategory(): WasteCategory = WasteCategory.INORGANIC
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.TextileContainer)
}
class GenericCleanPointWaste(override val tags: Set<String>) : Waste() {
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.CleanPoint())
    override fun getCategory(): WasteCategory = WasteCategory.NOT_DEFINED
}


class Batteries(override val tags: String = "baterías"): Waste() {
    override fun getCategory(): WasteCategory = WasteCategory.DANGEROUS
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.CleanPoint())
}

class ElectronicsAndSmallAppliances(override val tags: String = "Electrónica y electrodomésticos"): Waste() {
    override fun getCategory(): WasteCategory = WasteCategory.DANGEROUS
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.CleanPoint())
}

class GenericSigreWaste(override val tags: String) : Waste() {
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.SigrePoint)
    override fun getCategory(): WasteCategory = WasteCategory.NOT_DEFINED
}


class ExpiredMedicine(override val tags: String = "Expired medicines") : Waste() {
    override fun getCategory(): WasteCategory = WasteCategory.DANGEROUS
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.SigrePoint)
}

class UsedOil(override val tags: String = "aceites usados") : Waste() {
    override fun getCategory(): WasteCategory = WasteCategory.DANGEROUS
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.CleanPoint(),
        WasteProcessing.SpecificManagement("recogida en tiendas", "talleres, o establecimientos autorizados"))
}

class CleaningAndChemicalProduct(override val tags: String = "Productos de limpieza y químicos") : Waste() {
    override fun getCategory(): WasteCategory = WasteCategory.DANGEROUS
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.CleanPoint())
}

class GenericSpecificProcessingWaste(override val tags: String) : Waste() {
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.SpecificManagement())
    override fun getCategory(): WasteCategory = WasteCategory.NOT_DEFINED
}


class OldFurniture(override val tags: String = "Muebles viejos") : Waste() {
    override fun getCategory(): WasteCategory = WasteCategory.BULKY
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.SpecificManagement("Servicios municipales", "ayuntamientos"),
        WasteProcessing.CleanPoint())
}

class BigAppliances(override val tags: String = "Electrodomésticos grandes") : Waste() {
    override fun getCategory(): WasteCategory = WasteCategory.BULKY
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.SpecificManagement("Servicios municipales", "ayuntamientos"))
}

class DemolitionWaste(override val tags: String = "Residuos de contrucción y demolición") : Waste() {
    override fun getCategory(): WasteCategory = WasteCategory.CONSTRUCTION
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.SpecificManagement("Gestores autorizados de residuos", "Empresas de construcción"))
}

class PaintResidue(override val tags: String = "Residuos de pintura") : Waste() {
    override fun getCategory(): WasteCategory = WasteCategory.CONSTRUCTION
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.SpecificManagement("Gestores autorizados de residuos", "Empresas de construcción"))
}

class SanitaryWaste(override val tags: String = "Material biomédico y hospitalario"): Waste() {
    override fun getCategory(): WasteCategory = WasteCategory.SANITARY
    override fun getProcessing(): Set<WasteProcessing> = setOf(WasteProcessing.SpecificManagement(),
        WasteProcessing.CleanPoint())
}

