package com.example.feature.openai.cient.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.sse.EventSource
import okhttp3.sse.EventSources
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class OkHttpModule {

    // TODO OpenAIのトークンを貼る
    private val token = ""

    @Singleton
    @Provides
    fun providesRequestBuilder(): Request.Builder {
        return Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .header("Accept", "application/json")
            .addHeader("Authorization", "Bearer $token")
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.MINUTES)
            .connectTimeout(10, TimeUnit.MINUTES)
            .build()
    }
}