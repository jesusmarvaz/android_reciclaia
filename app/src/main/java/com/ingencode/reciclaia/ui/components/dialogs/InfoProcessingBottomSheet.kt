package com.ingencode.reciclaia.ui.components.dialogs

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.databinding.WasteProcessingInfoLayoutBinding
import com.ingencode.reciclaia.domain.model.WasteProcessing
import javax.inject.Inject

/**
Created with ❤ by jesusmarvaz on 2025-05-13.
 */

class InfoProcessingBottomSheet @Inject constructor() {
    fun launchInfoProcessing(wasteProcessing: WasteProcessing, a: AppCompatActivity) {
        /*val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.waste_processing_info_layout, null).apply {
            findViewById<ImageView>(R.id.iv_processing_image).apply { setImageResource(wasteProcessing.idDrawableSrc) }
            findViewById<TextView>(R.id.tv_processing_description).apply { text = getString(wasteProcessing.idStringDescription) }
        }
        bottomSheetDialog.apply { setContentView(view); show() }*/
        val bottomSheetDialog = BottomSheetDialog(a, R.style.BottomSheetDialogStyle)
        val binding = WasteProcessingInfoLayoutBinding.inflate(a.layoutInflater)

        binding.tvWasteTitle.setTextColor(ContextCompat.getColor(a, wasteProcessing.idColor))
        Glide.with(a).load(wasteProcessing.idDrawableSrc).into(binding.ivProcessingImage)
        //binding.ivProcessingImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), wasteProcessing.idDrawableSrc))
        binding.tvProcessingDescription.text = a.getString(wasteProcessing.idStringDescription)

        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.show()
    }
}