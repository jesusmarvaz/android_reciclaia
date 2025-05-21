package com.ingencode.reciclaia.data.repositories

import android.content.Context
import android.net.Uri
import com.ingencode.reciclaia.domain.model.ClassificationModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext

import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import javax.inject.Inject

/**
Created with ❤ by jesusmarvaz on 2025-05-20.
 */

class ModelInferenceProvider @Inject constructor(@ApplicationContext context: Context) : IAProviderInterface {
    private var tfLite: Interpreter
    init {
        tfLite = Interpreter(loadModelFile())
    }

    private fun loadModelFile(context: Context): MappedByteBuffer {
        var assetFileDescriptor = context.assets.openFd()
    }
    override suspend fun getClassificationFromInference(uri: Uri): ClassificationModel.ClassificationData {
        TODO("Not yet implemented")
    }
}