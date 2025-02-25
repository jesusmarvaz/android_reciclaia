package com.ingencode.reciclaia.utils.di

import com.ingencode.reciclaia.data.repositories.ISettingsRepository
import com.ingencode.reciclaia.data.repositories.SettingsRepository
import com.ingencode.reciclaia.ui.navigation.BackPressedListener
import com.ingencode.reciclaia.ui.navigation.IBackPressedListener
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-02.
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {
    companion object {
        @Singleton
        @Provides
        fun provideBackPressedListener(): IBackPressedListener {
            return BackPressedListener
        }
        /*@Singleton
        @Provides
        fun provideSettingsRepository(@ApplicationContext context: Context): ISettingsRepository {
            return SettingsRepository(context)
        }*/
    }

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(settingsRepository: SettingsRepository): ISettingsRepository
}