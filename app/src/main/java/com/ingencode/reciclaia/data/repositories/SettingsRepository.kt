package com.ingencode.reciclaia.data.repositories

import android.content.Context
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
    companion object {
        const val dark = "dark"
        const val light = "light"
        const val system = "system"
    }

}

class SettingsRepository @Inject constructor(@ApplicationContext context: Context): ISettingsRepository {
    private val sharedPreferencesKey:String = "my_shared_preferences"
    private val sharedPreferences = context.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)

    private val skipTutorialKey:String = "skip_tutorial"
    private val isViewVersionKey:String = "is_view_version_on"
    private val isIAProcessedLocallyKey:String = "is_ia_locally"
    private val themeModeKey:String = "theme_mode"
    override fun getSkipTutorial(): Boolean =
        sharedPreferences.getBoolean(this@SettingsRepository.skipTutorialKey, false)

    override fun setSkipTutorial(skipTutorial: Boolean) {
        sharedPreferences.edit().putBoolean(this.skipTutorialKey, skipTutorial).apply()
    }

    override fun getThemeMode(): String {
        val defValue = ISettingsRepository.system
        return sharedPreferences.getString(this@SettingsRepository.themeModeKey, defValue) ?: defValue
    }

    override fun setThemeMode(themeMode: String) {
        if (setOf<String>(ISettingsRepository.dark, ISettingsRepository.system, ISettingsRepository.light).contains(themeMode)) {
            sharedPreferences.edit().putString(this.themeModeKey, themeMode).apply()
        }
    }

    override fun getIsViewVersion(): Boolean =
        sharedPreferences.getBoolean(this.isViewVersionKey, true)

    override fun setIsViewVersion(isViewVersion: Boolean) =
        sharedPreferences.edit().putBoolean(this.isViewVersionKey, isViewVersion).apply()

    override fun getIsIAProcessedLocally(): Boolean {
        return sharedPreferences.getBoolean(this@SettingsRepository.isIAProcessedLocallyKey, true)
    }

    override fun setIsIAProcessedLocally(isIAProcessedLocally: Boolean) =
        sharedPreferences.edit().putBoolean(this.isIAProcessedLocallyKey, isIAProcessedLocally).apply()


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