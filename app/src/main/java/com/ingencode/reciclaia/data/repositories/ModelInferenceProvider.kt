package com.ingencode.reciclaia.data.repositories

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import com.ingencode.reciclaia.domain.model.ClassificationModel
import com.ingencode.reciclaia.domain.model.Tag
import com.ingencode.reciclaia.utils.ILog
import com.ingencode.reciclaia.utils.nameClass
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


class ModelInferenceProvider @Inject constructor(@ApplicationContext val context: Context)
    : IAProviderInterface, ILog {
    override fun theTag(): String = this.nameClass
    private val model: ClassificationModel.ModelInfo = ClassificationModel.ModelInfo("reciclaia_model.tflite", "1.0")
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
        // Verificar y convertir si es un HARDWARE bitmap
        val bitmapToProcess: Bitmap
        if (bitmap.config == Bitmap.Config.HARDWARE) {
            Log.d("ModelInference", "Bitmap is HARDWARE, converting to ARGB_8888.")
            bitmapToProcess = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            if (bitmapToProcess.byteCount == 0) throw Exception("La imagen no se ha procesado bien")
        } else { bitmapToProcess = bitmap }

        if (bitmapToProcess.width != 256 || bitmapToProcess.height != 256) {
            Log.w("ModelInference", "Bitmap no es 256x256 en convertBitmapToByteBuffer. Redimensionando...")
            // Esto es menos eficiente que hacerlo una sola vez antes
            val scaledBitmap = Bitmap.createScaledBitmap(bitmapToProcess, 256, 256, true)
            // Si redimensionaste, necesitas usar scaledBitmap para getPixels
            // y asegurarte de que intValues tenga el tamaño correcto (256*256)
            // Esta lógica se vuelve un poco más compleja si el redimensionamiento es condicional aquí.
            // Es mejor que el bitmap YA llegue con 256x256.
        }


        Log.d("ModelInference", "Bitmap dimensions for getPixels: ${bitmapToProcess.width}x${bitmapToProcess.height}, Config: ${bitmapToProcess.config}")


        val byteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3) // 4 bytes por float
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(256 * 256) // Asume que bitmapToProcess es 256x256
        bitmapToProcess.getPixels(intValues, 0, 256, 0, 0, 256, 256)

        for (pixel in intValues) {
            byteBuffer.putFloat(((pixel shr 16) and 0xFF).toFloat()) // Rojo
            byteBuffer.putFloat(((pixel shr 8) and 0xFF).toFloat()) // Verde
            byteBuffer.putFloat((pixel and 0xFF).toFloat()) // Azul
        }
        byteBuffer.rewind() // ¡Muy importante!
        return byteBuffer
    }

    private fun getMutableBitmap(bitmap: Bitmap): Bitmap {
        return bitmap.copy(Bitmap.Config.ARGB_8888, false)
    }

    @Throws(IOException::class)
    private fun inferImage(imageUri: Uri?): Pair<Int, Float> {
        val interpreter = loadModel()
        var bitmap: Bitmap = getBitmapFromUri(imageUri)!!
        val count = bitmap.byteCount
        if(count == 0) throw Exception("La imagen no se ha cargado")

        val inputBuffer: ByteBuffer? = convertBitmapToByteBuffer(bitmap)
        val output = Array<FloatArray?>(1) {
            FloatArray(Tag.entries.size) // Ajusta según el número de clases
        }

        interpreter.run(inputBuffer, output)

        val predictedClass = argmax(output[0]!!) // Encuentra la clase con mayor probabilidad
        logDebug("predictedClass: $predictedClass")
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
        val inferred: Pair<Int, Float> = inferImage(uri)
        val topPrediction = ClassificationModel.ClassificationPrediction(Tag.entries.sortedBy { it.tag }
            .map { it.tag }[inferred.first], confidence = inferred.second)
        return ClassificationModel.ClassificationData(arrayListOf(topPrediction), model = model, timestamp = System.currentTimeMillis())
    }
}