package com.ingencode.reciclaia.ui.screens.imagevisor

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.AlertDialog
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.data.remote.api.SealedResult
import com.ingencode.reciclaia.databinding.ActivityImagevisorBinding
import com.ingencode.reciclaia.ui.components.dialogs.AlertHelper
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
        observeViewModel()
        viewModel.setUri(uri)
        binding.apply {
            back.setOnClickListener { finish() }
            save.setOnClickListener { binding.composedVisor.getCroppedBitmap()?.let { askSaving(it) }}
            saveImageFolder.setOnClickListener { binding.composedVisor.getCroppedBitmap()?.let { askSharing(it) }}
        }
    }
    private fun observeViewModel() {
        with(viewModel) {
            uri.observe(this@ImageVisorActivity) {
                if (it != null) binding.composedVisor.apply { setImageUri(it) }
            }
            result.observe(this@ImageVisorActivity) {
                var message = ""
                val type = if (it is SealedResult.ResultSuccess<Uri>) {
                    message = "${getString(R.string.saved_successfully)}. Uri:${it.data.path}"
                    AlertHelper.Type.Success
                } else if (it is SealedResult.ResultError) {
                    message = "${getString(R.string.error_with_the_image_processing)}. ${it.error.message?.let{e-> "Error: $e"}}"
                    AlertHelper.Type.Error
                } else null
                type?.let {
                    val builder = AlertHelper.BottomAlertDialog.Builder(this@ImageVisorActivity, it, message)
                    builder.build().show()
                }
            }
        }

    }
    fun askSaving(bitmap: Bitmap) {
        val title = getString(R.string.confirm_action)
        val message = getString(R.string.want_saving)
        AlertHelper.Dialog.Builder(this, title, message) {
            viewModel.savedButtonPressed(bitmap)
        }
            .setNegativeText(getString(R.string.cancel))
            .setNegativeAction { Toast.makeText(this, getString(R.string.cancelled), Toast.LENGTH_SHORT).show() }
            .build().show()
    }

    fun askSharing(bitmap: Bitmap) {
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