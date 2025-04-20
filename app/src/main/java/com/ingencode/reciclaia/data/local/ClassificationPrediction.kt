package com.ingencode.reciclaia.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
Created with ❤ by jesusmarvaz on 2025-04-20.
 */

@Entity
data class ClassificationPrediction(@PrimaryKey(autoGenerate = true) val id: Int,
    val label:String,
    val confidence: Float)
