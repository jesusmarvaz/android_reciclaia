package com.ingencode.reciclaia.data.repositories

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import androidx.exifinterface.media.ExifInterface
import com.ingencode.reciclaia.data.remote.api.SealedResult
import com.ingencode.reciclaia.utils.SealedAppError
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import kotlin.math.abs

/**
Created with ❤ by jesusmarvaz on 2025-04-11.
 */

class LocalStorageProvider @Inject constructor(@ApplicationContext private val context: Context) {
    //companion object { const val APP_FILE_PROVIDER_AUTHORITY = "com.ingencode.reciclaia.fileprovider" }
    companion object {
        const val PREFIX_SAVED_LOCALLY_PROCESSED_IMAGE = "recicla_ia_processed_image_"
        const val PREFIX_EXPORTED_PROCESSED_IMAGE = "recicla_ia_exported_processed_image_"
    }

    fun getImageLocation(imageUri: Uri): Pair<Double, Double>? {
        var inputStream: InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(imageUri) ?: return null
            val exifInterface = ExifInterface(inputStream)
            val latLong = exifInterface.latLong ?: return null

            val latitude = latLong[0]
            val longitude = latLong[1]

            if(abs(latitude) > 90 || abs(longitude) > 180) return null

            return latitude to longitude
        } catch (e: IOException) {
            Log.e("getLatLongFromImage", "Error reading EXIF data: ${e.message}")
            return null
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                Log.e("getLatLongFromImage", "Error closing stream: ${e.message}")
            }
        }
    }
    fun getUriForCamera(): Uri? {
        val photoFile: File = createImageFile(context) ?: return null
        val authority = "${context.packageName}.fileprovider"
        val uri = FileProvider.getUriForFile(context, authority, photoFile)
        return uri
    }
    fun exportBitmapToNewFileInMediaStore(bitmap: Bitmap): SealedResult<Uri> {
        //val croppedBitmap = bitmap
        //val resizedBitmap = croppedBitmap.scale(512, 512)
        val fileName = "${PREFIX_EXPORTED_PROCESSED_IMAGE}${System.currentTimeMillis()}.jpg"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?: return SealedResult.ResultError(SealedAppError.ProblemSavingImagesLocally("Error inserting MediaStore values"))
        try {
            imageUri.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(it, values, null, null)
                return SealedResult.ResultSuccess<Uri>(imageUri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            imageUri.let { resolver.delete(it, null, null) } // Clean up failed insert
            return SealedResult.ResultError(SealedAppError.ProblemSavingImagesLocally(e.message))
        }
    }
    fun saveCroppedImage(bitmap: Bitmap, uri: Uri): Uri? {
        return try {
            val croppedBitmap = bitmap
            if (isAppUri(uri)) {
                Log.d("ComposedVisor", "Overwriting app-stored image: $uri")
                saveBitmapToUri(croppedBitmap, uri) // Overwrite if it's an app Uri
            } else {
                val resizedBitmap = croppedBitmap.scale(256, 256)
                val newFileName = "${PREFIX_SAVED_LOCALLY_PROCESSED_IMAGE}${System.currentTimeMillis()}.jpg"
                Log.d("ComposedVisor", "Saving new processed image: $newFileName")
                saveBitmapToAppStorage(context, resizedBitmap, newFileName) // Save to a new file
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun createImageFile(context: Context): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss",
            java.util.Locale.getDefault()).format(Date())
        // Use UUID to avoid the file exists
        val uniqueId = UUID.randomUUID().toString()
        val fileName = "camera_temp_file_${timeStamp}_${uniqueId}.jpg"

        val storageDir: File? = context.filesDir.resolve("Pictures") // Use internal files directory
        // Create the storage directory if it does not exist
        if (!storageDir?.exists()!!) {
            val success = storageDir.mkdirs()
            if (!success) {
                Log.e("createImageFile", "Failed to create storage directory.")
                return null
            }
        }

        return try {
            val imageFile = File(storageDir, fileName)
            // Log file location
            Log.d("createImageFile", "Temporary image file created at: ${imageFile.absolutePath}")
            imageFile
        } catch (ex: IOException) {
            // Error occurred while creating the File
            Log.e("createImageFile", "Error creating temporary image file: ${ex.message}")
            null
        }
    }
    private fun isAppUri(uri: Uri): Boolean {
        //return inputUri.authority == APP_FILE_PROVIDER_AUTHORITY
        if (uri.scheme == "file") {
            val path = uri.path
            if (path != null) {
                // Check if the path falls within your app's internal or external private storage
                val internalDirs = listOf(
                    context.filesDir,
                    context.cacheDir,
                    context.noBackupFilesDir
                )
                val externalFilesDir = context.getExternalFilesDir(null) // Or a more specific type, if needed

                if (internalDirs.any { path.startsWith(it?.absolutePath ?: "") }) {
                    Log.d("UriCheck", "Uri points to app's internal storage: $uri")
                    return true
                }

                if (externalFilesDir != null && path.startsWith(externalFilesDir.absolutePath)) {
                    Log.d("UriCheck", "Uri points to app's external private storage: $uri")
                    return true
                }
            }
        }

        Log.d("UriCheck", "Uri is NOT from app's private storage: $uri")
        return false // It's not from your app's storage
    }
    private fun saveBitmapToUri(bitmap: Bitmap, uri: Uri): Uri? {
        return try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            uri
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    private fun saveBitmapToAppStorage(context: Context, bitmap: Bitmap, fileName: String): Uri? {
        val directory: File? = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "reciclaia")
        if (directory != null) {
            if (!directory.exists()) directory.mkdirs()
            val file = File(directory, fileName)
            try {
                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                return Uri.fromFile(file)
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        } else {
            Log.e("ImageSaving", "External storage not available. Could not save image, saving in app files directory instead")
            return saveBitmapToNewFileInAppFilesDir(context, bitmap, fileName) // Fallback to internal storage
        }
    }
    private fun saveBitmapToNewFileInAppFilesDir(context: Context, bitmap: Bitmap, fileName: String): Uri? {
        val file = File(context.filesDir, fileName)
        return try {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}