package com.example.feature.openai.state

import com.example.feature.openai.model.GPT35TurboResponse
import okhttp3.Response

sealed interface SSEEvent {
    object Empty : SSEEvent
    object Open : SSEEvent
    data class Event(val response: GPT35TurboResponse) : SSEEvent
    data class Failure(val e: Throwable, val response: Response?) : SSEEvent
    object Closed : SSEEvent
}