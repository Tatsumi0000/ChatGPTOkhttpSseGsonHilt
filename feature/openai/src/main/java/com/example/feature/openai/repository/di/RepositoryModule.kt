package com.example.feature.openai.repository.di

import com.example.feature.openai.repository.OpenAiRepository
import com.example.feature.openai.repository.OpenAiRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule() {

    @Binds
    abstract fun bindOpenAiRepository(
        openAiRepositoryImpl: OpenAiRepositoryImpl
    ): OpenAiRepository
}