package com.ingencode.reciclaia.ui.screens.app.history

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.semantics.text
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.databinding.FragmentHistoryBinding
import com.ingencode.reciclaia.databinding.WasteProcessingInfoLayoutBinding
import com.ingencode.reciclaia.domain.model.ClassificationModel
import com.ingencode.reciclaia.domain.model.WasteProcessing
import com.ingencode.reciclaia.ui.components.FragmentBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.ui.components.dialogs.AlertHelper
import com.ingencode.reciclaia.ui.components.dialogs.InfoProcessingBottomSheet
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

@AndroidEntryPoint
class FragmentHistory() : FragmentBaseForViewmodel(), IProcessingInfoListener {
    private lateinit var binding: FragmentHistoryBinding
    private val viewmodel: HistoryViewmodel by viewModels()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapterHistory: ClassificationListAdapter
    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner
    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass

    @Inject
    lateinit var infoProcessingBottomSheet: InfoProcessingBottomSheet

    override fun getViewModelBase(): ViewModelBase? {
        return viewmodel
    }

    override fun observeVM() {
        with (viewmodel) {
            observableLoading().observe(this@FragmentHistory) {
                if (it) {
                    binding.rvItems.visibility = View.GONE
                    binding.noResultsYet.visibility = View.GONE
                }
            }
            classificationsOrdered.observe(this@FragmentHistory) { configureRV(it) }
            justDeleted.observe(this@FragmentHistory) {
                if (it != null) {
                    launchDeletedOk(viewmodel.deletedNItems.value ?: -1)
                    getClassifications()
                }
            }
            observableLoading().observe(this@FragmentHistory) {
                if (!it) binding.swipeRefreshLayoutUpdate.isRefreshing = false
            }
        }
    }

    override fun getPb(): ProgressBar? = binding.pbHorizontal.progressBarBase
    override fun getShaderLoading(): View? = binding.shader

    override fun initProperties() {
        layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        adapterHistory = ClassificationListAdapter(requireContext(), this)
        binding.title.tvScreenTitle.text = getString(R.string.history)
        binding.ivDelete.setOnClickListener {
            AlertHelper.Dialog
                .Builder(requireContext(), getString(R.string.confirm_action), getString(R.string.want_delete_all)) {
                    viewmodel.deleteAll()
                }.build().show()
        }
        binding.swipeRefreshLayoutUpdate.setOnRefreshListener { getClassifications() }
        binding.ivSort.setOnClickListener { launchSortOptions() }
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding
    }

    override fun onInfoClicked(wasteProcessing: WasteProcessing) {
        infoProcessingBottomSheet.launchInfoProcessing(wasteProcessing, requireActivity() as AppCompatActivity)
    }

    private fun configureRV(list: List<ClassificationModel>?) {
        if (list.isNullOrEmpty()) {
            binding.apply {
                noResultsYet.visibility = View.VISIBLE
                rvItems.visibility = View.GONE
                llDelete.visibility = View.GONE
                llOptions.visibility = View.GONE
            }
        } else {
            binding.apply {
                rvItems.visibility = View.VISIBLE
                noResultsYet.visibility = View.GONE
                rvItems.adapter = adapterHistory
                rvItems.layoutManager = layoutManager
                llDelete.visibility = View.VISIBLE
                llOptions.visibility = View.VISIBLE

            }
            adapterHistory.addAll(list)
        }
    }

    private fun launchDeletedOk(amount: Int) {
        AlertHelper.BottomAlertDialog.Builder(requireContext(), AlertHelper.Type.Success, getString(R.string.pattern_deleted_n_items).format(amount.toString()))
            .build()
            .show()
    }

    private fun getClassifications() = viewmodel.getClassifications()

    override fun onResume() {
        super.onResume()
        getClassifications()
    }

    private fun launchSortOptions() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.order_by_selector_layout, null)
        bottomSheetDialog.setContentView(view)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radio_group)
        radioGroup.apply {
                check(viewmodel.radioId.value ?: R.id.radio_none)
                setOnCheckedChangeListener { _, checkedId ->
                    viewmodel.setRadioId(checkedId)
            }
        }

        val checkboxDesc = view.findViewById<CheckBox>(R.id.checkbox_desc)
        checkboxDesc.apply {
            isChecked = viewmodel.descendingChecked.value == true
            setOnCheckedChangeListener { _, isChecked ->
                viewmodel.setIsDescendingChecked(isChecked)
            }
        }
        bottomSheetDialog.setOnDismissListener { viewmodel.getClassifications() }
        bottomSheetDialog.show()
    }

    /*private fun launchInfoProcessing(wasteProcessing: WasteProcessing) {
        /*val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.waste_processing_info_layout, null).apply {
            findViewById<ImageView>(R.id.iv_processing_image).apply { setImageResource(wasteProcessing.idDrawableSrc) }
            findViewById<TextView>(R.id.tv_processing_description).apply { text = getString(wasteProcessing.idStringDescription) }
        }
        bottomSheetDialog.apply { setContentView(view); show() }*/
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val binding = WasteProcessingInfoLayoutBinding.inflate(layoutInflater)

        binding.tvWasteTitle.setTextColor(ContextCompat.getColor(requireContext(), wasteProcessing.idColor))
        Glide.with(requireContext()).load(wasteProcessing.idDrawableSrc).into(binding.ivProcessingImage)
        //binding.ivProcessingImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), wasteProcessing.idDrawableSrc))
        binding.tvProcessingDescription.text = getString(wasteProcessing.idStringDescription)

        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.show()
    }*/
}