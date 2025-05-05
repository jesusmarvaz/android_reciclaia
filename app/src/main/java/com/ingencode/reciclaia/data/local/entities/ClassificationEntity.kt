package com.ingencode.reciclaia.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
Created with ❤ by jesusmarvaz on 2025-04-20.
 */

@Entity
data class ClassificationEntity(@PrimaryKey val id: String,
                                val uri: String,
                                val predictions: String,
                                @ColumnInfo("model_name") var modelName: String? = null,
                                @ColumnInfo("model_version") var modelVersion: String? = null,
                                var timestamp: Long? = null,
                                var title: String? = null,
                                var comments: String? = null,
                                var latitude: Double? = null,
                                var longitude: Double? = null)