package com.ingencode.reciclaia.ui.screens.imagevisor

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.data.remote.api.SealedResult
import com.ingencode.reciclaia.databinding.ActivityImagevisorBinding
import com.ingencode.reciclaia.domain.model.predictionsToText
import com.ingencode.reciclaia.domain.model.toText
import com.ingencode.reciclaia.ui.components.ActivityBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.ui.components.dialogs.AlertHelper
import com.ingencode.reciclaia.ui.screens.imagevisor.ImageVisorViewModel.Status
import com.ingencode.reciclaia.utils.SealedAppError
import com.ingencode.reciclaia.utils.getThemeColor
import com.ingencode.reciclaia.utils.nameClass
import com.ingencode.reciclaia.utils.toFormattedStringDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageVisorActivity : ActivityBaseForViewmodel() {
    private lateinit var binding: ActivityImagevisorBinding
    private var processMenuItem: MenuItem? = null
    private var saveDataMenuItem: MenuItem? = null
    private val viewModel: ImageVisorViewModel by viewModels()
    override fun goBack() = finish()

    private val requestLocationPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

    }

    override fun getTheTag(): String = this.nameClass

    override fun initProperties() {
        setSupportActionBar(binding.topAppBar)
        viewModel.manageIntent(intent)
        binding.apply {
            topAppBar.setNavigationOnClickListener { goBack() }
            tvPredictionValue.isSelected = true
            btProcess.setOnClickListener { viewModel.processButtonPressed(binding.composedVisor.getCroppedBitmap()!!) }
            ivLocationIcon.setOnClickListener { launchLocationPicker() }
        }
    }

    private fun launchLocationPicker() {
        if(!viewModel.getIsLocationEnabled()) {
            AlertHelper.BottomAlertDialog
                .Builder(this, AlertHelper.Type.Error, getString(R.string.permission_denied_check_app_settings))
                .setDelay(4000)
                .setAction { launchSettings() }
                .build().show()
            return
        }
        logDebug("launchLocationPicker")
        //TODO finish
    }

    private fun launchSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", this@ImageVisorActivity.packageName, null)
        }
        startActivity(intent)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val primaryColor = getThemeColor(android.R.attr.colorPrimary)
        menuInflater.inflate(R.menu.visor_menu, menu)
        processMenuItem = menu?.findItem(R.id.item_process) ?: return false
        tintMenuItemIcon(processMenuItem!!, primaryColor)

        // Tint the "save_data" icon
        saveDataMenuItem = menu.findItem(R.id.item_save_data) ?: return false
        tintMenuItemIcon(saveDataMenuItem!!, primaryColor)
        return true
    }

    private fun tintMenuItemIcon(menuItem: MenuItem, color: Int) {
        val drawable = menuItem.icon
        if (drawable != null) {
            val wrappedDrawable = DrawableCompat.wrap(drawable).mutate()
            DrawableCompat.setTint(wrappedDrawable, color)
            menuItem.icon = wrappedDrawable
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val bitmap = binding.composedVisor.getCroppedBitmap() ?: return false
        val actionError: ()-> Boolean = {
            AlertHelper.BottomAlertDialog.Builder(this@ImageVisorActivity, AlertHelper.Type.Error, getString(R.string.error_with_the_image_processing))
            false
        }

        return when (item.itemId) {
            R.id.item_process -> { viewModel.processButtonPressed(bitmap); true}
            R.id.item_save_data -> {
                bitmap?.let {
                    askSavingClassificationData(it, binding.title.text.toString(),
                        binding.comments.text.toString()
                    )
                    return true
                }?: actionError.invoke()
            }
            R.id.item_save_pic -> {
                bitmap?.let {
                    askStoringImage(it)
                    return true
                }?: actionError.invoke()
            }
            else -> false
        }
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = ActivityImagevisorBinding.inflate(layoutInflater)
        return binding
    }

    private fun classificationResultsVisibility(areVisible: Boolean) {
        val visibility = if(areVisible) View.VISIBLE else View.GONE
        binding.scrollviewResults.visibility = visibility
        binding.noClassifiedYetView.visibility = if(binding.scrollviewResults.isVisible) View.GONE else View.VISIBLE
    }

    private fun observeResult() {
        viewModel.classificationResult.observe(this@ImageVisorActivity) {
            val uri = viewModel.getUriFromResult(it)
            if (it != null && uri != null) binding.composedVisor.apply { setImageUri(uri) }
            viewModel.getClassificationFromResult(it)?.let {
                binding.tvPredictionValue.text = it.classificationData?.topPrediction?.toText()
                binding.tvModelValue.text = it.classificationData?.model?.toText()
                binding.tvPredictionsExtendedValue.text = it.classificationData?.predictionsToText()
                binding.title.setText(it.title)
                binding.comments.setText(it.comments)
                binding.tvDatetimeValue.text = it.classificationData?.timestamp?.toFormattedStringDate()
                binding.tvLocationValue.text = it.location?.toString() ?: ""
                val colorBackground = viewModel.getClassificationBackgroundColor(this@ImageVisorActivity)
                val colorText = viewModel.getClassificationTextColor(this@ImageVisorActivity)
                if (colorBackground != null && colorText != null) {
                    binding.clClassificationResult.backgroundTintList = ColorStateList.valueOf(colorBackground)
                    binding.tvPredictionValue.setTextColor(colorText)
                }
            }
            if (it is SealedResult.ResultError) {
                val message = "${getString(R.string.error_with_the_image_processing)}. ${if (it.error is SealedAppError && it.error.message != null) "Error: ${it.error.message}" else ""}}"
                val type = AlertHelper.Type.Error
                val builder = AlertHelper.BottomAlertDialog.Builder(this@ImageVisorActivity, type, message)
                builder.build().show()
            }
        }
    }

    private fun observeStatus() {
        viewModel.status.observe(this@ImageVisorActivity) {
            binding.tvStatus.text = it.name

            processMenuItem?.apply {
                val enabled = it in setOf<Status>(Status.NO_RESULTS, Status.NOT_CLASSIFIED_YET, Status.SAVED_NOT_CLASSIFIED, Status.CLASSIFIED_NOT_SAVED)
                isEnabled = enabled
                isVisible = enabled
            }
            saveDataMenuItem?.apply {
                val enabled = it in setOf<Status>(Status.CLASSIFIED_NOT_SAVED, Status.SAVED_NOT_CLASSIFIED, Status.CLASSIFIED_SAVED)
                isEnabled = enabled
                isVisible = enabled
            }
            binding.title.isEnabled = it in setOf<Status>(Status.CLASSIFIED_NOT_SAVED, Status.CLASSIFIED_SAVED)
            binding.comments.isEnabled = it in setOf<Status>(Status.CLASSIFIED_NOT_SAVED, Status.CLASSIFIED_SAVED)
            binding.composedVisor.isEditable = it != Status.CLASSIFIED_SAVED
            classificationResultsVisibility(it in setOf<Status>(Status.CLASSIFIED_SAVED, Status.CLASSIFIED_NOT_SAVED))

        }
    }

    private fun observeError() {
        viewModel.observableSealedError().observe(this@ImageVisorActivity) {
            if (it != null && it is SealedAppError.ProblemSavingImagesLocally) {
                var message = getString(R.string.error_with_the_image_processing)
                var type: AlertHelper.Type = AlertHelper.Type.Error
                val builder = AlertHelper.BottomAlertDialog.Builder(this@ImageVisorActivity, type, message)
                builder.build().show()
            }
            if (it is SealedAppError) {
                val toast: Toast = Toast.makeText(this@ImageVisorActivity, "${it.message}", Toast.LENGTH_LONG)
                toast.show()
            }
        }
    }

    private fun observeExportedSuccessfully() {
        viewModel.exportedSuccessfully.observe(this@ImageVisorActivity) {
            if (it != null) {
                val message = getString(R.string.exported_successfully)
                val type = AlertHelper.Type.Success
                val builder = AlertHelper.BottomAlertDialog.Builder(this@ImageVisorActivity, type, message)
                builder.build().show()
            }
        }
    }

    private fun observeDataSavedSuccessfully() {
        viewModel.dataSavedSuccessfully.observe(this@ImageVisorActivity) {
            if (it != null) {
                val message = getString(R.string.data_saved)
                val type = AlertHelper.Type.Success
                val builder = AlertHelper.BottomAlertDialog.Builder(this@ImageVisorActivity, type, message)
                builder.build().show()
            }
        }
    }

    override fun observeVM() {
        observeStatus()
        observeResult()
        observeError()
        observeExportedSuccessfully()
        observeDataSavedSuccessfully()
    }

    fun askSavingClassificationData(bitmap: Bitmap, title: String? = null, comments: String? = null) {
        val title = getString(R.string.confirm_action)
        val message = getString(R.string.want_saving)
        AlertHelper.Dialog.Builder(this, title, message) {
            viewModel.savedButtonPressed(bitmap, title, comments)
        }
            .setNegativeText(getString(R.string.cancel))
            .setNegativeAction { Toast.makeText(this, getString(R.string.cancelled), Toast.LENGTH_SHORT).show() }
            .build().show()
    }

    fun askStoringImage(bitmap: Bitmap) {
        val title = getString(R.string.confirm_action)
        val message = getString(R.string.want_sharing_to_local_media)
        AlertHelper.Dialog.Builder(this, title, message) {
            viewModel.shareToLocalMediaFolderButtonPressed(bitmap)
        }
            .setNegativeText(getString(R.string.cancel))
            .setNegativeAction { Toast.makeText(this, getString(R.string.cancelled), Toast.LENGTH_SHORT).show() }
            .build().show()
    }

    override fun getViewLifeCycleOwner(): LifecycleOwner = this
    override fun getViewModelBase(): ViewModelBase? = viewModel
    override fun getPb(): ProgressBar? = binding.shaderAndPb.progressBarBase
    override fun getShaderLoading(): View? = binding.shaderAndPb.shader
}