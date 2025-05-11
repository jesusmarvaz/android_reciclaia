package com.ingencode.reciclaia.domain.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
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
enum class Tag(val tag: String, @StringRes idStringName: Int) {
    ORGANIC("organic", R.string.organic), FOOD("food", R.string.food), FOOD_WASTE("food_waste", R.string.food_waste),
    COFFEE_GROUNDS("coffee_grounds", R.string.coffee_grounds), EGGSHELLS("eggshells", R.string.eggshells),
    AEROSOL_CANS("aerosol_cans", R.string.aerosol_cans), STYROFOAM_FOOD_CONTAINERS("styrofoam_food_containers", R.string.styrofoam_food_containers),
    DISPOSABLE_PLASTIC_CUTLERY("disposable_plastic_cutlery", R.string.disposable_plastic_cutlery),
    TEA_BAGS("tea_bags", R.string.tea_bags), ALUMINIUM_FOOD_CANS("aluminium_food_cans", R.string.aluminium_food_cans),
    ALUMINIUM_SODA_CANS("aluminium_soda_cans", R.string.aluminium_soda_cans), PLASTIC_SODA_BOTTLES("plastic_soda_bottles", R.string.plastic_soda_bottles),
    CAN("can", R.string.can), PLASTIC_CUP_LIDS("plastic_cup_lids", R.string.plastic_cup_lids),
    PLASTIC_DETERGENT_BOTTLES("plastic_detergent_bottles", R.string.plastic_detergent_bottles),
    PLASTIC_FOOD_CONTAINERS("plastic_food_containers", R.string.plastic_food_containers),
    PLASTIC_SHOPPING_BAGS("plastic_shopping_bags", R.string.plastic_shopping_bags),
    PLASTIC_STRAWS("plastic_straws", R.string.plastic_straws), PLASTIC_TRASH_BAGS("plastic_trash_bags", R.string.plastic_trash_bags),
    PLASTIC_WATER_BOTTLES("plastic_water_bottles", R.string.plastic_water_bottles), STEEL_FOOD_CANS("steel_food_cans", R.string.steel_food_cans),
    PAPER("paper", R.string.paper), CARDBOARD("cardboard", R.string.cardboard), CARDBOARD_BOXES("cardboard_boxes", R.string.cardboard_boxes),
    CARDBOARD_PACKAGING("cardboard_packaging", R.string.cardboard_packaging), MAGAZINES("magazines", R.string.magazines), NEWSPAPER("newspaper", R.string.newspaper),
    OFFICE_PAPER("office_paper", R.string.office_paper), PAPER_CUPS("paper_cups", R.string.paper_cups), GLASS("glass", R.string.glass),
    GLASS_BEVERAGE("glass_beverage_bottles", R.string.glass_beverage_bottles), GLASS_COSMETICS_CONTAINER("glass_cosmetics_containers", R.string.glass_cosmetics_containers),
    GLASS_FOOD_JARS("glass_food_jars", R.string.glass_food_jars), OIL("oil", R.string.oil), ELECTRONICS("electronics", R.string.electronics),
    TEXTIL("textil", R.string.textil), CLOTHING("clothing", R.string.clothing), SHOES("shoes", R.string.shoes), MEDICINE("medicine", R.string.medicine),
    MISCELLANEOUS("miscellaneous", R.string.miscellaneous)
}
enum class WasteTagCategory(val tags: Set<Tag>, val processing: Set<WasteProcessing>) {
    ORGANIC(setOf(Tag.ORGANIC, Tag.FOOD, Tag.FOOD_WASTE, Tag.COFFEE_GROUNDS, Tag.EGGSHELLS), setOf(WasteProcessing.OrganicRestContainer)),
    AEROSOLS(setOf(Tag.AEROSOL_CANS), setOf(WasteProcessing.YellowContainer, WasteProcessing.CleanPoint())),
    CLEAN_POINT_WASTE(setOf(Tag.STYROFOAM_FOOD_CONTAINERS, Tag.ELECTRONICS), setOf(WasteProcessing.CleanPoint())),
    REST(setOf(Tag.DISPOSABLE_PLASTIC_CUTLERY, Tag.TEA_BAGS), setOf(WasteProcessing.RestContainer)),
    CONTAINER(setOf(Tag.AEROSOL_CANS, Tag.ALUMINIUM_FOOD_CANS, Tag.ALUMINIUM_SODA_CANS,
        Tag.PLASTIC_SODA_BOTTLES, Tag.CAN, Tag.PLASTIC_CUP_LIDS, Tag.PLASTIC_DETERGENT_BOTTLES,
        Tag.PLASTIC_FOOD_CONTAINERS, Tag.PLASTIC_SHOPPING_BAGS, Tag.PLASTIC_STRAWS, Tag.PLASTIC_TRASH_BAGS,
        Tag.PLASTIC_WATER_BOTTLES, Tag.STEEL_FOOD_CANS), setOf(WasteProcessing.YellowContainer)),
    PAPER(setOf(Tag.PAPER, Tag.CARDBOARD, Tag.CARDBOARD_BOXES, Tag.CARDBOARD_PACKAGING, Tag.MAGAZINES, Tag.NEWSPAPER, Tag.OFFICE_PAPER, Tag.PAPER_CUPS), setOf(WasteProcessing.BlueContainer)),
    GLASS(setOf(Tag.GLASS, Tag.GLASS_BEVERAGE, Tag.GLASS_COSMETICS_CONTAINER, Tag.GLASS_FOOD_JARS), setOf(WasteProcessing.GreenContainer)),
    USED_OIL(setOf(Tag.OIL), setOf(WasteProcessing.CleanPoint(), WasteProcessing.SpecificManagement(R.string.special_recycling, R.string.municipal_services_special_containers))),
    TEXTIL(setOf(Tag.TEXTIL, Tag.CLOTHING, Tag.SHOES), setOf(WasteProcessing.TextileContainer)),
    MEDICINE(setOf(Tag.MEDICINE), setOf(WasteProcessing.SigrePoint)),
    MISCELLANEOUS(setOf(Tag.MISCELLANEOUS), setOf(WasteProcessing.CleanPoint(), WasteProcessing.RestContainer, WasteProcessing.SpecificManagement()))
}

//enum class WasteCategory() { ORGANIC, INORGANIC, DANGEROUS, BULKY, CONSTRUCTION, SANITARY, NOT_DEFINED }
sealed class WasteProcessing (@ColorRes val idColor: Int, @StringRes val idString: Int, @DrawableRes val idDrawableRes: Int) {
    object OrganicRestContainer : WasteProcessing(R.color.organic_container, R.string.organic_container, R.drawable.bin_no_recyclable)
    object YellowContainer : WasteProcessing(R.color.yellow_container, R.string.yellow_container, R.drawable.bin_recyclable)
    object BlueContainer : WasteProcessing(R.color.blue_container, R.string.blue_container, R.drawable.bin_recyclable)
    object GreenContainer : WasteProcessing(R.color.green_container, R.string.green_container, R.drawable.bin_recyclable)
    object TextileContainer : WasteProcessing(R.color.textile_container, R.string.textile_container, R.drawable.bin_recyclable)
    object SigrePoint: WasteProcessing(R.color.sigre_point, R.string.sigre_point, R.drawable.bin_no_recyclable)
    object RestContainer: WasteProcessing(R.color.rest_container, R.string.rest_container, R.drawable.bin_no_recyclable)
    data class CleanPoint(val description: String? = null) : WasteProcessing(R.color.clean_point, R.string.clean_point, R.drawable.clean_point)
    data class SpecificManagement(@StringRes val description: Int? = null, @StringRes val managedBy: Int? = null) : WasteProcessing(R.color.specific_management, R.string.specific_management, R.drawable.bin_recyclable)
}

