package com.ingencode.reciclaia.ui.viewmodels

import com.ingencode.reciclaia.data.repositories.ISettingsRepository
import com.ingencode.reciclaia.ui.components.ViewModelBase
import com.ingencode.reciclaia.utils.nameClass
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-23.
 */

@HiltViewModel
class SettingsViewModel @Inject constructor(private val settingsRepository: ISettingsRepository) : ViewModelBase() {
    override fun theTag(): String = this.nameClass
    /*
    private val _themeMode: MutableLiveData<String> = MutableLiveData(ISettingsRepository.system)
    val themeMode: LiveData<String> = _themeMode
    private val _isIAProcessedLocally: MutableLiveData<Boolean> = MutableLiveData(true)
    val isIAProcessedLocally: LiveData<Boolean> = _isIAProcessedLocally*/

    fun getSkipTutorial(): Boolean{
        val skipTutorial = settingsRepository.getSkipTutorial()
        return skipTutorial
    }

    fun setSkipTutorial(skipTutorial: Boolean) {
        settingsRepository.setSkipTutorial(skipTutorial)
    }
    fun getThemeMode(): String = settingsRepository.getThemeMode()
    fun setThemeMode(themeMode: String) = settingsRepository.setThemeMode(themeMode)
    fun getIsViewVersion(): Boolean = settingsRepository.getIsViewVersion()
    fun setIsViewVersion(isViewVersion: Boolean) = settingsRepository.setIsViewVersion(isViewVersion)
    fun getIsIAProcessedLocally(): Boolean = settingsRepository.getIsIAProcessedLocally()
    fun setIsIAProcessedLocally(isProcessedLocally: Boolean) = settingsRepository.setIsIAProcessedLocally(isProcessedLocally)
}