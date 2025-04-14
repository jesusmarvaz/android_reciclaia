package com.ingencode.reciclaia.ui.screens.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.databinding.FragmentAppBinding
import com.ingencode.reciclaia.ui.components.FragmentBase
import com.ingencode.reciclaia.ui.screens.imagevisor.ImageVisorActivity
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.AndroidEntryPoint

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-12.
 */

@AndroidEntryPoint
class FragmentApp : FragmentBase() {
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: FragmentAppBinding
    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass

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
                    openCameraVisor(uri)
                }
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
        view.findViewById<ImageView>(R.id.iv_source_camera).setOnClickListener { bottomSheetDialog.dismiss(); openCameraVisor() }
        view.findViewById<ImageView>(R.id.iv_source_gallery).setOnClickListener { bottomSheetDialog.dismiss(); openGallery() }
        bottomSheetDialog.show()
    }

    private fun openCameraVisor(uri:Uri? = null) {
        logDebug("openCamera clicked, uri: ${uri?.toString()}")
        val intent = Intent(requireContext(), ImageVisorActivity::class.java)
        intent.putExtra("uri", uri)
        startActivity(intent)
    }

    private fun openGallery() {
        logDebug("openGallery clicked")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //  You might want to add a chooser:
         val chooser = Intent.createChooser(intent, "Select Image")
        imagePickerLauncher.launch(chooser)
    }
}