package com.example.namessearchapp.dagger

import com.example.namessearchapp.data.INamesRepository
import com.example.namessearchapp.data.NamesDemoRepository
import com.example.namessearchapp.demo.demoNames
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NamesDemoRepositoryModule {

    @Provides
    fun provideNamesDemoRepositoryService() = NamesDemoRepository(demoNamesList = demoNames)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NamesRepositoryModule{

    @Binds
    abstract fun bindNamesRepository(itemsSource: NamesDemoRepository): INamesRepository
}