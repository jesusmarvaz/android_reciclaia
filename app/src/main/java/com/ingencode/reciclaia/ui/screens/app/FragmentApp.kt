package com.ingencode.reciclaia.ui.screens.app

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IdRes
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.data.repositories.ISettingsRepository
import com.ingencode.reciclaia.data.repositories.LocalStorageProvider
import com.ingencode.reciclaia.databinding.FragmentAppBinding
import com.ingencode.reciclaia.ui.components.FragmentBase
import com.ingencode.reciclaia.ui.components.dialogs.AlertHelper
import com.ingencode.reciclaia.ui.screens.app.home.IImageSelector
import com.ingencode.reciclaia.ui.screens.imagevisor.ImageVisorActivity
import com.ingencode.reciclaia.ui.viewmodels.ImageLauncherViewModel
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-12.
 */

@AndroidEntryPoint
class FragmentApp : FragmentBase(), IImageSelector {
    companion object {
        const val NAVIGATION_DESTINATION_TAG = "navigation_destination_tag"
    }
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var photoUri: Uri? = null
    private lateinit var binding: FragmentAppBinding
    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass
    private lateinit var imageVisorActivityResultLauncher: ActivityResultLauncher<Intent>
    @Inject lateinit var settingsRepository: ISettingsRepository
    @Inject lateinit var localStorageProvider: LocalStorageProvider
    private lateinit var navHostFragment: NavHostFragment
    private val sharedViewModel: ImageLauncherViewModel by activityViewModels()

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) pickFromCamera()
            else {
                AlertHelper.BottomAlertDialog.Builder(
                    requireContext(),
                    AlertHelper.Type.Error, getString(R.string.no_camera_permission_granted)
                )
                    .setAction {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", requireContext().packageName, null)
                        }
                        startActivity(intent)
                    }.build().show()
            }
        }

    private fun navigate(@IdRes id: Int) {
        binding.bnvApp.selectedItemId = id
    }
    /*class MyActivity : ComponentActivity() {
    private val myBroadcastReceiver = MyBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...
        ContextCompat.registerReceiver(this, myBroadcastReceiver, filter, receiverFlags)
        setContent { MyApp() }
    }

    override fun onDestroy() {
        super.onDestroy()
        // When you forget to unregister your receiver here, you're causing a leak!
        this.unregisterReceiver(myBroadcastReceiver)
    }
}*/



    override fun initProperties() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.launchImageSelectorEvent.collect {
                    launchImageSelector()
                }
            }
        }
        navHostFragment =
            childFragmentManager.findFragmentById(R.id.appNavFragment) as NavHostFragment
        binding.bnvApp.setupWithNavController(navHostFragment.navController)
        binding.favAdd.setOnClickListener {
            launchImageSelector()
        }

        imageVisorActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                result.data?.getIntExtra(NAVIGATION_DESTINATION_TAG, -1)?.let { navigate(it) }
            }
        }

        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri = result.data?.data
                imageUri?.let { uri ->
                    startImageVisorActivity(uri)
                }
            }
        }

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
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

    override fun launchImageSelector() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.image_source_selector_layout, null)
        bottomSheetDialog.setContentView(view)
        //  Configurar listeners y otros elementos de la UI en la vista 'view':
        //  view.findViewById<Button>(R.id.my_button).setOnClickListener { ... }
        view.findViewById<ImageView>(R.id.iv_source_camera)
            .setOnClickListener { bottomSheetDialog.dismiss(); pickFromCamera() }
        view.findViewById<ImageView>(R.id.iv_source_gallery)
            .setOnClickListener { bottomSheetDialog.dismiss(); pickFromGallery() }
        bottomSheetDialog.show()
    }

    private fun startImageVisorActivity(uri: Uri? = null) {
        logDebug("openCamera clicked, uri: ${uri?.toString()}")
        val intent = Intent(requireContext(), ImageVisorActivity::class.java)
        intent.putExtra("uri", uri)
        imageVisorActivityResultLauncher.launch(intent)
    }

    private fun pickFromCamera() {
        if (!settingsRepository.checkCameraPermission()) {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
            return
        }
        logDebug("pick from camera selected")

        photoUri = localStorageProvider.getUriForCamera()
        photoUri?.let { cameraLauncher.launch(it) }
    }

    private fun pickFromGallery() {
        logDebug("pick from gallery selected")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //  You might want to add a chooser:
        val chooser = Intent.createChooser(intent, "Select Image")
        imagePickerLauncher.launch(chooser)
    }
}