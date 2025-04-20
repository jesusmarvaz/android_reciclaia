package com.ingencode.reciclaia.ui.screens.app.settings

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IdRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.data.remote.api.Routes
import com.ingencode.reciclaia.data.repositories.ISettingsRepository
import com.ingencode.reciclaia.databinding.FragmentSettingsBinding
import com.ingencode.reciclaia.ui.components.FragmentBaseForViewmodel
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.ui.navigation.NavHostFragments
import com.ingencode.reciclaia.ui.screens.app.FragmentAppDirections
import com.ingencode.reciclaia.ui.screens.tutorial.Tutorial
import com.ingencode.reciclaia.ui.theme.MyComposeWrapper
import com.ingencode.reciclaia.ui.viewmodels.SettingsViewModel
import com.ingencode.reciclaia.utils.applyTheme
import com.ingencode.reciclaia.utils.getPackageInfoCompat
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import com.ingencode.reciclaia.ui.components.dialogs.AlertHelper

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

@AndroidEntryPoint
class FragmentSettings : FragmentBaseForViewmodel() {
    private lateinit var binding: FragmentSettingsBinding
    private val settingsViewModel: SettingsViewModel by viewModels()
    private var showTutorial by mutableStateOf(false)

    private val requestLocationPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            settingsViewModel.checkLocationAvailability()
        }

    override fun getViewLifeCycleOwner(): LifecycleOwner = viewLifecycleOwner
    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass
    override fun getViewModelBase(): ViewModelBase? = null
    override fun getPb(): ProgressBar? = null
    override fun getShaderLoading(): View? = null
    override fun observeVM() {
        settingsViewModel.showLocationPermissionRequestButton.observe(this) {
            it?.let {
                binding.checkboxLocation.isEnabled = it
                binding.checkboxLocation.isChecked = if (!it) false else settingsViewModel.getIsLocationEnabled()
                binding.btStartLocationChecker.visibility = if (it) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        settingsViewModel.checkLocationAvailability()
    }

    override fun initProperties() {
        binding.title.tvScreenTitle.text = getString(R.string.settings)
        binding.appVersion.setDetail(
            String.format(
                "v%s",
                context?.packageName?.let {
                    context?.packageManager?.getPackageInfoCompat(
                        it,
                        0
                    )?.versionName
                })
        )

        binding.checkboxTutorial.apply {
            isChecked = !settingsViewModel.getSkipTutorial()
            setOnCheckedChangeListener { _, isChecked ->
                settingsViewModel.setSkipTutorial(!isChecked)
                checkAvailabilityOfComposeCheckBox()
            }
        }

        binding.checkboxCompose.apply {
            checkAvailabilityOfComposeCheckBox()
            isChecked = !settingsViewModel.getIsViewVersion()
            setOnCheckedChangeListener { _, isChecked ->
                settingsViewModel.setIsViewVersion(!isChecked)
            }
        }

        binding.btStartTutorial.setOnClickListener {
            launchTutorial()
        }

        val radioCheckedId = mapThemeToRadioId(settingsViewModel.getThemeMode())

        binding.radioGroup.apply {
            check(radioCheckedId)
            setOnCheckedChangeListener { _, checkedId ->
                val theme = mapRadioIdToTheme(checkedId)
                settingsViewModel.setThemeMode(theme)
                applyTheme(theme)
            }
        }

        val dlProcessorLocation =
            mapDlLocationToRadioId(settingsViewModel.getIsIAProcessedLocally())

        binding.radioGroupDl.apply {
            check(dlProcessorLocation)
            setOnCheckedChangeListener { _, checkedId ->
                val isDLLocally = mapRadioIdToDlLocation(checkedId)
                settingsViewModel.setIsIAProcessedLocally(isDLLocally)
            }
        }

        binding.checkboxLocation.apply {
            isChecked = settingsViewModel.getIsLocationEnabled()
            setOnCheckedChangeListener { _, isChecked ->
                settingsViewModel.setIsLocationEnabled(isChecked)
            }
        }

        binding.btStartLocationChecker.setOnClickListener {
            if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                requestLocationPermissions.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            } else {
                AlertHelper.BottomAlertDialog
                    .Builder(requireContext(), AlertHelper.Type.Error, getString(R.string.permission_denied_check_app_settings))
                    .setDelay(4000)
                    .setAction { launchSettings() }
                    .build().show()
            }
        }

        binding.contactUs.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_SENDTO,
                    Uri.fromParts("mailto", Routes.SUPPORT_EMAIL, null)
                )
            )
        }

        binding.rateUs.setOnClickListener {
            val uri =
                Uri.parse("market://details?id=" + context?.packageName) //or using the Ktx extension, as below
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        ("http://play.google.com/store/apps/details?id=" + context?.packageName).toUri()
                    )
                )
            }
        }

        binding.privacy.setOnClickListener {
            requireActivity().findNavController(NavHostFragments.HOST.idNavHostFragment)
                .navigate(
                    FragmentAppDirections.actionFragmentAppToFragmentWeb(
                        title = getString(R.string.privacy_policy),
                        url = Routes.WEB_PRIVACY, enumnavhostfragment = NavHostFragments.HOST
                    )
                )
        }

        binding.terms.setOnClickListener {
            requireActivity().findNavController(NavHostFragments.HOST.idNavHostFragment)
                .navigate(
                    FragmentAppDirections.actionFragmentAppToFragmentWeb(
                        title = getString(R.string.terms_and_conditions),
                        url = Routes.WEB_TERMS, enumnavhostfragment = NavHostFragments.HOST
                    )
                )
        }

        binding.composeView.setContent {
            if (showTutorial) {
                MyComposeWrapper {
                    ShowBottomSheetTutorial({
                        showTutorial = false
                    }) {
                        Tutorial()
                    }
                }
            }
        }
    }

    private fun launchTutorial() {
        showTutorial = true
    }

    private fun launchSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }

    private fun checkAvailabilityOfComposeCheckBox() {
        binding.checkboxCompose.isEnabled = settingsViewModel.getSkipTutorial()
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding
    }

    private fun mapThemeToRadioId(theme: String): Int {
        return when (theme) {
            ISettingsRepository.system -> binding.radioSystem.id
            ISettingsRepository.light -> binding.radioLight.id
            ISettingsRepository.dark -> binding.radioDark.id
            else -> {
                binding.radioSystem.id
            }
        }
    }

    private fun mapDlLocationToRadioId(isDLProcessedLocally: Boolean): Int {
        return if (isDLProcessedLocally) binding.radioLocal.id else binding.radioRemote.id
    }

    private fun mapRadioIdToDlLocation(@IdRes id: Int): Boolean {
        return when (id) {
            binding.radioLocal.id -> true
            else -> false
        }
    }

    private fun mapRadioIdToTheme(@IdRes id: Int): String {
        return when (id) {
            binding.radioSystem.id -> ISettingsRepository.system
            binding.radioLight.id -> ISettingsRepository.light
            binding.radioDark.id -> ISettingsRepository.dark
            else -> ISettingsRepository.system
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowBottomSheetTutorial(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(true) }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    onDismissRequest()
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Contenido del Tutorial")
                    content.invoke()
                    Button(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                                onDismissRequest()
                            }
                        }
                    }) {
                        Text(text = "Cerrar")
                    }
                }
            }
        }
    }
}