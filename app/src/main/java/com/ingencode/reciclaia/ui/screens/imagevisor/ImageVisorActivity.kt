package com.ingencode.reciclaia.ui.screens.imagevisor

import android.graphics.Bitmap
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.data.remote.api.SealedResult
import com.ingencode.reciclaia.databinding.ActivityImagevisorBinding
import com.ingencode.reciclaia.domain.model.ClassificationModel
import com.ingencode.reciclaia.ui.components.ActivityBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.ui.components.dialogs.AlertHelper
import com.ingencode.reciclaia.utils.SealedAppError
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageVisorActivity : ActivityBaseForViewmodel() {
    private lateinit var binding: ActivityImagevisorBinding
    private val viewModel: ImageVisorViewModel by viewModels()
    override fun goBack() = finish()

    override fun getTheTag(): String = this.nameClass

    override fun initProperties() {
        val uri = intent.getParcelableExtra("uri", Uri::class.java) ?: return
        setSupportActionBar(binding.topAppBar)
        viewModel.setUri(uri)
        binding.apply {
            topAppBar.setNavigationOnClickListener { goBack() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.visor_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val bitmap = binding.composedVisor.getCroppedBitmap()
        val actionError: ()-> Boolean = {
            AlertHelper.BottomAlertDialog.Builder(this@ImageVisorActivity, AlertHelper.Type.Error, getString(R.string.error_with_the_image_processing))
            false
        }

        return when (item.itemId) {
            R.id.item_process -> { viewModel.processButtonPressed(); true}
            R.id.item_save_data -> {
                bitmap?.let {
                    askSavingClassificationData(it)
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

    override fun observeVM() {
        with(viewModel) {
            classificationResult.observe(this@ImageVisorActivity) {
                val uri = getUriFromResult()
                if (it != null && uri != null) binding.composedVisor.apply { setImageUri(uri) }
            }

            this.observableSealedError().observe(this@ImageVisorActivity) {
                if (it != null && it is SealedAppError.ProblemSavingImagesLocally) {
                    var message = getString(R.string.error_with_the_image_processing)
                    var type: AlertHelper.Type = AlertHelper.Type.Error
                    val builder = AlertHelper.BottomAlertDialog.Builder(this@ImageVisorActivity, type, message)
                    builder.build().show()
                }
            }

            exportedSuccessfully.observe(this@ImageVisorActivity) {
                if (it != null) {
                    val message = getString(R.string.exported_successfully)
                    val type = AlertHelper.Type.Success
                    val builder = AlertHelper.BottomAlertDialog.Builder(this@ImageVisorActivity, type, message)
                    builder.build().show()
                }
            }

            dataSavedSuccessfully.observe(this@ImageVisorActivity) {
                if (it != null) {
                    val message = getString(R.string.data_saved)
                    val type = AlertHelper.Type.Success
                    val builder = AlertHelper.BottomAlertDialog.Builder(this@ImageVisorActivity, type, message)
                    builder.build().show()
                }
            }
            classificationResult.observe(this@ImageVisorActivity) {
                if (it is SealedResult.ResultError) {
                    val message = "${getString(R.string.error_with_the_image_processing)}. ${if (it.error is SealedAppError && it.error.message != null) "Error: ${it.error.message}" else ""}}"
                    val type = AlertHelper.Type.Error
                    val builder = AlertHelper.BottomAlertDialog.Builder(this@ImageVisorActivity, type, message)
                    builder.build().show()
                }
                if (it is SealedResult.ResultSuccess) {

                }
            }
        }
    }

    fun askSavingClassificationData(bitmap: Bitmap) {
        val title = getString(R.string.confirm_action)
        val message = getString(R.string.want_saving)
        AlertHelper.Dialog.Builder(this, title, message) {
            viewModel.savedButtonPressed(bitmap)
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