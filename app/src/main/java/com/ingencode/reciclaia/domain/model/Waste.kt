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
enum class Tag(val tag: String, @StringRes val idStringName: Int) {
    AEROSOL_CANS("aerosol_cans", R.string.aerosol_cans),
    ALUMINIUM_FOOD_CANS("aluminium_food_cans", R.string.aluminium_food_cans),
    ALUMINIUM_SODA_CANS("aluminium_soda_cans", R.string.aluminium_soda_cans),
    CARDBOARD_BOXES("cardboard_boxes", R.string.cardboard_boxes),
    CARDBOARD_PACKAGING("cardboard_packaging", R.string.cardboard_packaging),
    CLOTHING("clothing", R.string.clothing),
    COFFEE_GROUNDS("coffee_grounds", R.string.coffee_grounds),
    DISPOSABLE_PLASTIC_CUTLERY("disposable_plastic_cutlery", R.string.disposable_plastic_cutlery),
    EGGSHELLS("eggshells", R.string.eggshells),
    FOOD_WASTE("food_waste", R.string.food_waste),
    GLASS_BEVERAGE("glass_beverage_bottles", R.string.glass_beverage_bottles),
    GLASS_COSMETICS_CONTAINER("glass_cosmetics_containers", R.string.glass_cosmetics_containers),
    GLASS_FOOD_JARS("glass_food_jars", R.string.glass_food_jars),
    MAGAZINES("magazines", R.string.magazines),
    NEWSPAPER("newspaper", R.string.newspaper),
    OFFICE_PAPER("office_paper", R.string.office_paper),
    PAPER_CUPS("paper_cups", R.string.paper_cups),
    PLASTIC_CUP_LIDS("plastic_cup_lids", R.string.plastic_cup_lids),
    PLASTIC_DETERGENT_BOTTLES("plastic_detergent_bottles", R.string.plastic_detergent_bottles),
    PLASTIC_FOOD_CONTAINERS("plastic_food_containers", R.string.plastic_food_containers),
    PLASTIC_SHOPPING_BAGS("plastic_shopping_bags", R.string.plastic_shopping_bags),
    PLASTIC_SODA_BOTTLES("plastic_soda_bottles", R.string.plastic_soda_bottles),
    PLASTIC_STRAWS("plastic_straws", R.string.plastic_straws),
    PLASTIC_TRASH_BAGS("plastic_trash_bags", R.string.plastic_trash_bags),
    PLASTIC_WATER_BOTTLES("plastic_water_bottles", R.string.plastic_water_bottles),
    SHOES("shoes", R.string.shoes),
    STEEL_FOOD_CANS("steel_food_cans", R.string.steel_food_cans),
    STYROFOAM_CUPS("styrofoam_cups", R.string.styrofoam_cups),
    STYROFOAM_FOOD_CONTAINERS("styrofoam_food_containers", R.string.styrofoam_food_containers),
    TEA_BAGS("tea_bags", R.string.tea_bags)
}
enum class WasteTagCategory(val tags: Set<Tag>, val processing: Set<WasteProcessing>) {
    ORGANIC(setOf(Tag.FOOD_WASTE, Tag.COFFEE_GROUNDS, Tag.EGGSHELLS), setOf(WasteProcessing.OrganicRestContainer)),
    AEROSOLS(setOf(Tag.AEROSOL_CANS), setOf(WasteProcessing.YellowContainer, WasteProcessing.CleanPoint())),
    CLEAN_POINT_WASTE(setOf(Tag.STYROFOAM_FOOD_CONTAINERS, Tag.STYROFOAM_CUPS), setOf(WasteProcessing.CleanPoint())),
    REST(setOf(Tag.DISPOSABLE_PLASTIC_CUTLERY, Tag.TEA_BAGS), setOf(WasteProcessing.RestContainer)),
    CONTAINER(setOf(Tag.AEROSOL_CANS, Tag.ALUMINIUM_FOOD_CANS, Tag.ALUMINIUM_SODA_CANS,
        Tag.PLASTIC_SODA_BOTTLES, Tag.PLASTIC_CUP_LIDS, Tag.PLASTIC_DETERGENT_BOTTLES,
        Tag.PLASTIC_FOOD_CONTAINERS, Tag.PLASTIC_SHOPPING_BAGS, Tag.PLASTIC_STRAWS, Tag.PLASTIC_TRASH_BAGS,
        Tag.PLASTIC_WATER_BOTTLES, Tag.STEEL_FOOD_CANS), setOf(WasteProcessing.YellowContainer)),
    PAPER(setOf(Tag.CARDBOARD_BOXES, Tag.CARDBOARD_PACKAGING, Tag.MAGAZINES, Tag.NEWSPAPER, Tag.OFFICE_PAPER, Tag.PAPER_CUPS), setOf(WasteProcessing.BlueContainer)),
    GLASS(setOf(Tag.GLASS_BEVERAGE, Tag.GLASS_COSMETICS_CONTAINER, Tag.GLASS_FOOD_JARS), setOf(WasteProcessing.GreenContainer)),
    TEXTILE(setOf(Tag.CLOTHING, Tag.SHOES), setOf(WasteProcessing.TextileContainer))
}

//enum class WasteCategory() { ORGANIC, INORGANIC, DANGEROUS, BULKY, CONSTRUCTION, SANITARY, NOT_DEFINED }
sealed class WasteProcessing (@ColorRes val idColor: Int, @StringRes val idStringTitle: Int,
                              @StringRes val idStringDescription: Int, @DrawableRes val idDrawableSrc: Int) {
    object OrganicRestContainer : WasteProcessing(R.color.organic_container, R.string.organic_container, R.string.organic_container_description, R.drawable.img_contenedor_marron)
    object YellowContainer : WasteProcessing(R.color.yellow_container, R.string.yellow_container, R.string.yellow_container_description, R.drawable.img_amarillo_contenedor)
    object BlueContainer : WasteProcessing(R.color.blue_container, R.string.blue_container, R.string.blue_container_description, R.drawable.img_contenedor_azul)
    object GreenContainer : WasteProcessing(R.color.green_container, R.string.green_container, R.string.green_container_description, R.drawable.img_contendor_verde)
    object TextileContainer : WasteProcessing(R.color.textile_container, R.string.textile_container, R.string.textile_container_description, R.drawable.textil)
    object SigrePoint: WasteProcessing(R.color.sigre_point, R.string.sigre_point, R.string.sigre_point_description, R.drawable.sigre)
    object RestContainer: WasteProcessing(R.color.rest_container, R.string.rest_container, R.string.rest_container_description, R.drawable.img_contenedor_gris)
    data class CleanPoint(val description: String? = null) : WasteProcessing(R.color.clean_point, R.string.clean_point, R.string.clean_point_description, R.drawable.img_punto_limpio)
    data class SpecificManagement(@StringRes val description: Int? = null, @StringRes val managedBy: Int? = null) : WasteProcessing(R.color.specific_management, R.string.specific_management, R.string.specific_management_description, R.drawable.especiales)
}

