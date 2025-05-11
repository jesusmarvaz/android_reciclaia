package com.ingencode.reciclaia.utils.di

import android.content.Context
import com.ingencode.reciclaia.data.repositories.ClassificationRepositoryImpl
import com.ingencode.reciclaia.data.repositories.IClassificationRepository
import com.ingencode.reciclaia.data.repositories.ISettingsRepository
import com.ingencode.reciclaia.data.repositories.LocalStorageProvider
import com.ingencode.reciclaia.data.repositories.SettingsRepository
import com.ingencode.reciclaia.ui.navigation.BackPressedListener
import com.ingencode.reciclaia.ui.navigation.IBackPressedListener
import com.ingencode.reciclaia.ui.screens.app.history.ClassificationListAdapter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-02.
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(settingsRepository: SettingsRepository): ISettingsRepository

    @Binds
    @Singleton
    abstract fun bindProcessedImageModelRepository(processedImageModel: ClassificationRepositoryImpl): IClassificationRepository

    /*@Binds
    @Singleton
    abstract fun bindWasteRepository(wasteRepository: WasteRepository): IWasteRepository*/
}

@Module
@InstallIn(SingletonComponent::class)
object CommonModuleImplementation {

    @Provides
    @Singleton
    fun provideLocalStorageProvider(@ApplicationContext context: Context): LocalStorageProvider {
        return LocalStorageProvider(context)
    }

    @Provides
    @Singleton
    fun provideBackPressedListener(): IBackPressedListener {
        return BackPressedListener
    }

    @Provides
    @Singleton
    fun provideClassificationListAdapter(@ApplicationContext context: Context): ClassificationListAdapter {
        return ClassificationListAdapter(context)
    }
}