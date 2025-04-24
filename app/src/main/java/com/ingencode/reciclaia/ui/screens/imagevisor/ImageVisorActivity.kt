package com.ingencode.reciclaia.ui.screens.imagevisor

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.data.remote.api.SealedResult
import com.ingencode.reciclaia.databinding.ActivityImagevisorBinding
import com.ingencode.reciclaia.domain.model.ClassificationModel
import com.ingencode.reciclaia.ui.components.dialogs.AlertHelper
import com.ingencode.reciclaia.utils.SealedAppError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageVisorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImagevisorBinding
    private val viewModel: ImageVisorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagevisorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        val uri = intent.getParcelableExtra("uri", Uri::class.java) ?: return
        setSupportActionBar(binding.topAppBar)
        observeViewModel()
        viewModel.setUri(uri)
        binding.apply {
            topAppBar.setNavigationOnClickListener { finish() }
            save.setOnClickListener { binding.composedVisor.getCroppedBitmap()?.let { askSavingClassificationData(it) }}
            storeImageToFolder.setOnClickListener { binding.composedVisor.getCroppedBitmap()?.let { askStoringImage(it) }}
        }
    }

    private fun observeViewModel() {
        with(viewModel) {
            classificationResult.observe(this@ImageVisorActivity) {
                val uri = getUriFromResult()
                if (it != null && uri != null) binding.composedVisor.apply { setImageUri(uri) }
                //TODO binding.save.visibility = if ((it as SealedResult.ResultSuccess<ClassificationModel>).data.predictions.isEmpty() != true) View.VISIBLE else View.GONE
            }
            exportedSuccessfully.observe(this@ImageVisorActivity) {
                var message = ""
                var type: AlertHelper.Type = AlertHelper.Type.Error

                if (it != null && it == true) {
                    message = getString(R.string.exported_successfully)
                    type = AlertHelper.Type.Success
                }
                if (it != null && it == false) {
                    message = getString(R.string.error_with_the_image_saving)
                }
                val builder = AlertHelper.BottomAlertDialog.Builder(this@ImageVisorActivity, type, message)
                builder.build().show()
            }
            classificationResult.observe(this@ImageVisorActivity) {
                var message = ""
                val type = if (it is SealedResult.ResultSuccess<ClassificationModel>) {
                    message = "${getString(R.string.saved_successfully)}. Uri:${it.data.uri}"
                    AlertHelper.Type.Success
                } else if (it is SealedResult.ResultError) {
                    message = "${getString(R.string.error_with_the_image_processing)}. ${if (it.error is SealedAppError && it.error.message != null) "Error: ${it.error.message}" else ""}}"
                    AlertHelper.Type.Error
                } else null
                type?.let {
                    val builder = AlertHelper.BottomAlertDialog.Builder(this@ImageVisorActivity, it, message)
                    builder.build().show()
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
}