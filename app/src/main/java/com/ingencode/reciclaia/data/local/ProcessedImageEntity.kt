package com.ingencode.reciclaia.data.local

import android.location.Location
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ingencode.reciclaia.domain.model.ProcessedImageModel.ClassificationPrediction
import com.ingencode.reciclaia.domain.model.ProcessedImageModel.ModelInfo

/**
Created with ❤ by jesusmarvaz on 2025-04-20.
 */

@Entity
data class ProcessedImageEntity(@PrimaryKey(autoGenerate = true) val id: Int = 0,
                                val uri: Uri,
                                var model: ModelInfo? = null,
                                var timeStamp: Long? = null,
                                var title: String? = null,
                                var comments: String? = null,
                                var latitude: Double? = null,
                                var longitude: Double? = null)