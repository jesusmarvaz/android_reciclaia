package com.ingencode.reciclaia.domain.model

import android.net.Uri

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-05-15.
 */
data class HomeClassificationModel(val uri: Uri, val id: String, val tag: String, val confidence: Float)
data class HomeTop3(val wasteTag: Tag, val amount: String)
data class HomeModel(val listLast10: ArrayList<HomeClassificationModel>, val averageConfidence: Float, val total: Int,
                     val fixedArrayTop3: Array<HomeTop3?> = arrayOfNulls(3)) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HomeModel

        if (averageConfidence != other.averageConfidence) return false
        if (listLast10 != other.listLast10) return false
        if (!fixedArrayTop3.contentEquals(other.fixedArrayTop3)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = averageConfidence.hashCode()
        result = 31 * result + listLast10.hashCode()
        result = 31 * result + fixedArrayTop3.contentHashCode()
        return result
    }
}
