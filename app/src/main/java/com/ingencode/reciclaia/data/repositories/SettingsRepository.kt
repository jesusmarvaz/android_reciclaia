package com.ingencode.reciclaia.data.repositories

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-22.
 */
interface ISettingsRepository {
    fun getSkipTutorial(): Boolean
    fun setSkipTutorial(skipTutorial: Boolean)
    fun getThemeMode(): String
    fun setThemeMode(themeMode: String)
    fun getIsViewVersion(): Boolean
    fun setIsViewVersion(isViewVersion: Boolean)
    fun getIsIAProcessedLocally(): Boolean
    fun setIsIAProcessedLocally(isIAProcessedLocally: Boolean)
    fun setLocationEnabled(isEnabled: Boolean)
    fun getIsLocationEnabled(): Boolean

    fun getLocationAvailability(): Boolean
    fun checkCameraPermission(): Boolean

    companion object {
        const val dark = "dark"
        const val light = "light"
        const val system = "system"
    }

}

class SettingsRepository @Inject constructor(@ApplicationContext val context: Context): ISettingsRepository {
    private val sharedPreferencesKey:String = "my_shared_preferences"
    private val sharedPreferences = context.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)

    private val skipTutorialKey:String = "skip_tutorial"
    private val isViewVersionKey:String = "is_view_version_on"
    private val isIAProcessedLocallyKey:String = "is_ia_locally"
    private val themeModeKey:String = "theme_mode"
    private val isLocationEnabledKey:String = "location_enabled"
    override fun getSkipTutorial(): Boolean =
        sharedPreferences.getBoolean(this@SettingsRepository.skipTutorialKey, false)

    override fun setSkipTutorial(skipTutorial: Boolean) {
        sharedPreferences.edit { putBoolean(this@SettingsRepository.skipTutorialKey, skipTutorial) }
    }

    override fun getThemeMode(): String {
        val defValue = ISettingsRepository.system
        return sharedPreferences.getString(this@SettingsRepository.themeModeKey, defValue) ?: defValue
    }

    override fun setThemeMode(themeMode: String) {
        if (setOf<String>(ISettingsRepository.dark,
                ISettingsRepository.system,
                ISettingsRepository.light).contains(themeMode)) {
            sharedPreferences.edit { putString(this@SettingsRepository.themeModeKey, themeMode) }
        }
    }

    override fun getIsViewVersion(): Boolean =
        sharedPreferences.getBoolean(this@SettingsRepository.isViewVersionKey, true)

    override fun setIsViewVersion(isViewVersion: Boolean) =
        sharedPreferences.edit { putBoolean(this@SettingsRepository.isViewVersionKey, isViewVersion) }

    override fun getIsIAProcessedLocally(): Boolean {
        return sharedPreferences.getBoolean(this@SettingsRepository.isIAProcessedLocallyKey, true)
    }

    override fun setIsIAProcessedLocally(isIAProcessedLocally: Boolean) =
        sharedPreferences.edit {
            putBoolean(this@SettingsRepository.isIAProcessedLocallyKey, isIAProcessedLocally)
        }

    override fun getIsLocationEnabled(): Boolean =
        sharedPreferences.getBoolean(this@SettingsRepository.isLocationEnabledKey, true)

    override fun setLocationEnabled(isEnabled: Boolean) =
        sharedPreferences.edit { putBoolean(this@SettingsRepository.isLocationEnabledKey, isEnabled)}

    override fun getLocationAvailability(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    //It is not necessary for now use Coroutine Flows
    /*override fun getSkipTutorial(): Flow<Boolean> = flow {
        emit(sharedPreferences.getBoolean(this@SettingsRepository.skipTutorialKey, false))
    }

    override suspend fun setSkipTutorial(skipTutorial: Boolean) {
        sharedPreferences.edit().putBoolean(this.viewVersionKey, true).apply()
    }

    override fun getThemeMode(): Flow<String> = flow {
        val defValue = ISettingsRepository.system
        emit(sharedPreferences.getString(this@SettingsRepository.themeModeKey, defValue) ?: defValue)
    }

    override suspend fun setThemeMode(themeMode: String) {
        sharedPreferences.edit().putString(this.themeModeKey, themeMode).apply()
    }

    override fun getIsIAProcessedLocally(): Flow<Boolean> = flow {
        emit(sharedPreferences.getBoolean(this@SettingsRepository.isIAProcessedLocallyKey, true))
    }

    override suspend fun setIsIAProcessedLocally(isIAProcessedLocally: Boolean) {
        sharedPreferences.edit().putBoolean(this.isIAProcessedLocallyKey, isIAProcessedLocally).apply()
    }*/
}