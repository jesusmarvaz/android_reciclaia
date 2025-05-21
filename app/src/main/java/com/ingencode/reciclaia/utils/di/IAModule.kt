package com.ingencode.reciclaia.utils.di

import com.ingencode.reciclaia.data.repositories.IAProviderInterface
import com.ingencode.reciclaia.data.repositories.IAProviderMockImp
import com.ingencode.reciclaia.data.repositories.ModelInferenceProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
Created with ❤ by jesusmarvaz on 2025-04-24.
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class IAModule {
    //TODO change it to the proper implementation
    @Binds
    //abstract fun bindsIAProvider(iaProvider: IAProviderMockImp): IAProviderInterface
    abstract fun bindsIAProvider(iaProvider: ModelInferenceProvider): IAProviderInterface
}

/*@Module
@InstallIn(SingletonComponent::class)
object IAModuleImplementation {

    @Provides
    fun provideIAProvider(): IAProviderInterface {
        return IAProviderMockImp()
    }
}*/