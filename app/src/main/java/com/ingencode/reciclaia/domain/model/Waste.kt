package com.ingencode.reciclaia.domain.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.ingencode.reciclaia.R

/**
Created with ❤ by jesusmarvaz on 2025-05-05.
 */

//https://github.com/pedropro/TACO/blob/master/demo.ipynb
//https://www.kaggle.com/datasets/jatayu000/better-garbage-data?resource=download
//https://www.kaggle.com/datasets/alistairking/recyclable-and-household-waste-classification/data
/*
* The dataset covers the following waste categories and items:
- Plastic: water bottles, soda bottles, detergent bottles, shopping bags, trash bags, food containers, disposable cutlery, straws, cup lids
- Paper and Cardboard: newspaper, office paper, magazines, cardboard boxes, cardboard packaging
- Glass: beverage bottles, food jars, cosmetic containers
- Metal: aluminum soda cans, aluminum food cans, steel food cans, aerosol cans
- Organic Waste: food waste (fruit peels, vegetable scraps), eggshells, coffee grounds, tea bags
- Textiles: clothing, shoes*/
enum class WasteTagCategory(val tagS: Set<String>, val processing: Set<WasteProcessing>) {
    ORGANIC(setOf("organic", "food", "food_waste", "coffee_grounds", "eggshells"), setOf(WasteProcessing.OrganicRestContainer)),
    AEROSOLS(setOf("aerosol_cans"), setOf(WasteProcessing.YellowContainer, WasteProcessing.CleanPoint())),
    CLEAN_POINT(setOf("styrofoam_food_containers"), setOf(WasteProcessing.CleanPoint())),
    REST(setOf("disposable_plastic_cutlery", "tea_bags"), setOf(WasteProcessing.RestContainer)),
    CONTAINER(setOf("aerosol_cans", "aluminium_food_cans", "aluminium_soda_cans",
        "plastic_soda_bottles", "plastic", "can", "cans", "carton", "plastic_cup_lids", "plastic_detergent_bottles",
        "plastic_food_containers", "plastic_shopping_bags", "plastic_straws", "plastic_trash_bags", "plastic_water_bottles", "steel_food_cans"), setOf(WasteProcessing.YellowContainer)),
    PAPER(setOf("paper", "cardboard", "cardboard_boxes", "cardboard_packaging", "magazines", "newspaper", "office_paper", "paper_cups"), setOf(WasteProcessing.BlueContainer)),
    GLASS(setOf("glass", "glass_beverage_bottles", "glass_cosmetics_containers", "glass_food_jars"), setOf(WasteProcessing.GreenContainer)),
    USED_OIL(setOf("oil"), setOf(WasteProcessing.CleanPoint(), WasteProcessing.SpecificManagement(R.string.special_recycling, R.string.municipal_services_special_containers))),
    ELECTRONICS(setOf("electronics"), setOf(WasteProcessing.CleanPoint())),
    TEXTIL(setOf("textil", "clothing", "shoes"), setOf(WasteProcessing.TextileContainer)),
    MEDICINE(setOf("medicine"), setOf(WasteProcessing.SigrePoint)),
    MISCELLANEOUS(setOf("miscellaneous"), setOf(WasteProcessing.Unknown, WasteProcessing.RestContainer, WasteProcessing.SpecificManagement()))
}

//enum class WasteCategory() { ORGANIC, INORGANIC, DANGEROUS, BULKY, CONSTRUCTION, SANITARY, NOT_DEFINED }
sealed class WasteProcessing (@ColorRes val idColor: Int, @StringRes val nameStringResource: Int) {
    object OrganicRestContainer : WasteProcessing(R.color.organic_container, R.string.organic_container)
    object YellowContainer : WasteProcessing(R.color.yellow_container, R.string.yellow_container)
    object BlueContainer : WasteProcessing(R.color.blue_container, R.string.blue_container)
    object GreenContainer : WasteProcessing(R.color.green_container, R.string.green_container)
    object TextileContainer : WasteProcessing(R.color.textile_container, R.string.textile_container)
    object SigrePoint: WasteProcessing(R.color.sigre_point, R.string.sigre_point)
    object RestContainer: WasteProcessing(R.color.rest_container, R.string.rest_container)
    object Unknown: WasteProcessing(R.color.rest_container, R.string.rest_container)
    data class CleanPoint(val description: String? = null) : WasteProcessing(R.color.clean_point, R.string.clean_point)
    data class SpecificManagement(@StringRes val description: Int? = null, @StringRes val managedBy: Int? = null) : WasteProcessing(R.color.specific_management, R.string.specific_management)
    data class HazardousProcessing(@StringRes val description: Int? = null, @StringRes val managedBy: Int? = null) : WasteProcessing(R.color.hazardous_processing, R.string.hazardous_processing)
}

