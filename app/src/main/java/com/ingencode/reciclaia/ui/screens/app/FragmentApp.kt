package com.ingencode.reciclaia.ui.screens.app

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.text.intl.Locale
import androidx.core.content.FileProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.data.repositories.ISettingsRepository
import com.ingencode.reciclaia.databinding.FragmentAppBinding
import com.ingencode.reciclaia.ui.components.FragmentBase
import com.ingencode.reciclaia.ui.screens.imagevisor.ImageVisorActivity
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import kotlin.text.format

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-12.
 */

@AndroidEntryPoint
class FragmentApp : FragmentBase() {
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var photoUri: Uri? = null
    private lateinit var binding: FragmentAppBinding
    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass

    @Inject
    lateinit var settingsRepository: ISettingsRepository

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            pickFromCamera()
        }

    override fun initProperties() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.appNavFragment) as NavHostFragment
        binding.bnvApp.setupWithNavController(navHostFragment.navController)
        binding.favAdd.setOnClickListener {
            launchImageSelector()
        }

        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri = result.data?.data
                imageUri?.let { uri ->
                    startImageVisorActivity(uri)
                }
            }
        }

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
            if (isSuccessful) {
                // The photo is saved in photoUri
                photoUri?.let { startImageVisorActivity(it) }
            }
        }

        binding.favAdd.setOnClickListener {
            launchImageSelector()
        }
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentAppBinding.inflate(layoutInflater)
        return binding
    }
    private fun launchImageSelector() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.image_source_selector_layout, null)
        bottomSheetDialog.setContentView(view)
        //  Configurar listeners y otros elementos de la UI en la vista 'view':
        //  view.findViewById<Button>(R.id.my_button).setOnClickListener { ... }
        view.findViewById<ImageView>(R.id.iv_source_camera).setOnClickListener { bottomSheetDialog.dismiss(); pickFromCamera() }
        view.findViewById<ImageView>(R.id.iv_source_gallery).setOnClickListener { bottomSheetDialog.dismiss(); pickFromGallery() }
        bottomSheetDialog.show()
    }

    private fun startImageVisorActivity(uri:Uri? = null) {
        logDebug("openCamera clicked, uri: ${uri?.toString()}")
        val intent = Intent(requireContext(), ImageVisorActivity::class.java)
        intent.putExtra("uri", uri)
        startActivity(intent)
    }

    private fun pickFromCamera() {
        if (!settingsRepository.checkCameraPermission()) {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
            return
        }
        logDebug("pick from camera selected")
        val photoFile: File? = createImageFile(requireContext())

        photoFile?.also { file ->
            photoUri = getUriForImage(requireContext(), file) ?: return@also
            cameraLauncher.launch(photoUri!!)
        }
    }

    private fun getUriForImage(context: Context, file: File): Uri? {
        val authority = "${context.packageName}.fileprovider"
        return FileProvider.getUriForFile(context, authority, file)
    }

    private fun pickFromGallery() {
        logDebug("pick from gallery selected")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //  You might want to add a chooser:
         val chooser = Intent.createChooser(intent, "Select Image")
        imagePickerLauncher.launch(chooser)
    }

    private fun createImageFile(context: Context): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss",
            java.util.Locale.ITALY).format(Date())
        val storageDir: File? = context.filesDir?.resolve("Pictures") // Use internal files directory
        // Create the storage directory if it does not exist
        if (!storageDir?.exists()!!) {
            val success = storageDir.mkdirs()
            if (!success) {
                Log.e("createImageFile", "Failed to create storage directory.")
                return null
            }
        }

        return try {
            File.createTempFile(
                "camera_temp_file_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                Log.d("createImageFile", "Temporary image file created at: $absolutePath")
            }
        } catch (ex: IOException) {
            // Error occurred while creating the File
            Log.e("createImageFile", "Error creating temporary image file: ${ex.message}")
            null
        }
    }
}