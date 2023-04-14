package com.example.feature.openai.usecase.di

import com.example.feature.openai.usecase.OpenAiUseCase
import com.example.feature.openai.usecase.OpenAiUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindOpenAiUseCase(
        openAiUseCaseImpl: OpenAiUseCaseImpl
    ): OpenAiUseCase
}