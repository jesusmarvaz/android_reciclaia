package com.ingencode.reciclaia.data.repositories

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import com.ingencode.reciclaia.domain.model.ClassificationModel
import com.ingencode.reciclaia.domain.model.Tag
import dagger.hilt.android.qualifiers.ApplicationContext
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import javax.inject.Inject


/**
Created with ❤ by jesusmarvaz on 2025-05-20.
 */


class ModelInferenceProvider @Inject constructor(@ApplicationContext val context: Context) : IAProviderInterface {
    private val model: ClassificationModel.ModelInfo = ClassificationModel.ModelInfo("mi_modelo.tflite", "1.0")

    @Throws(IOException::class)
    private fun loadModel(): Interpreter {
        val fileDescriptor: AssetFileDescriptor = context.assets.openFd(model.modelName)
        val inputStream = FileInputStream(fileDescriptor.getFileDescriptor())
        val fileChannel = inputStream.getChannel()
        val startOffset = fileDescriptor.getStartOffset()
        val declaredLength = fileDescriptor.getDeclaredLength()
        val modelBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        return Interpreter(modelBuffer)
    }

    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri?): Bitmap? {
        //return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        val source = ImageDecoder.createSource(context.getContentResolver(), uri!!)
        return ImageDecoder.decodeBitmap(source)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(256 * 256)
        bitmap.getPixels(intValues, 0, 256, 0, 0, 256, 256)

        for (pixel in intValues) {
            byteBuffer.putFloat(((pixel shr 16) and 0xFF) / 255.0f) // Rojo
            byteBuffer.putFloat(((pixel shr 8) and 0xFF) / 255.0f) // Verde
            byteBuffer.putFloat((pixel and 0xFF) / 255.0f) // Azul
        }

        return byteBuffer
    }

    private fun getMutableBitmap(bitmap: Bitmap): Bitmap {
        return bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    @Throws(IOException::class)
    private fun inferImage(imageUri: Uri?): Pair<Int, Float> {
        val interpreter = loadModel()
        var bitmap: Bitmap = getBitmapFromUri(imageUri)!!
        bitmap = getMutableBitmap(bitmap)
        bitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, true)

        val inputBuffer: ByteBuffer? = convertBitmapToByteBuffer(bitmap)
        val output =
            Array<FloatArray?>(1) { FloatArray(Tag.entries.size) }  // Ajusta según el número de clases

        interpreter.run(inputBuffer, output)
        interpreter.run()

        val predictedClass = argmax(output[0]!!) // Encuentra la clase con mayor probabilidad
        return Pair(predictedClass, output[0]?.get(predictedClass) ?: 0f)
    }

    private fun argmax(array: FloatArray): Int {
        var maxIndex = 0
        for (i in 1..<array.size) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i
            }
        }
        return maxIndex
    }

    override suspend fun getClassificationFromInference(uri: Uri): ClassificationModel.ClassificationData {
        val inferred = inferImage(uri)
        val topPrediction = ClassificationModel.ClassificationPrediction(Tag.entries.sortedBy { it.tag }.map { it.tag }[inferred.first], confidence = inferred.second)
        return ClassificationModel.ClassificationData(arrayListOf(topPrediction), model = model)
    }
}