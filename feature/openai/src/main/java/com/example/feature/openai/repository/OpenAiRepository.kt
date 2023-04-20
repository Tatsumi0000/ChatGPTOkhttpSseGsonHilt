package com.example.feature.openai.repository

import com.example.feature.openai.model.GPT35Turbo
import com.example.feature.openai.model.GPT35TurboResponse
import com.example.feature.openai.state.SSEEvent
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

interface OpenAiRepository {
    suspend fun postCompletions(gpt35Turbo: GPT35Turbo)
    fun cancelCompletions()
    val state: SharedFlow<SSEEvent>
}

@Singleton
class OpenAiRepositoryImpl @Inject constructor(
    private val requestBuilder: Request.Builder,
    private val client: OkHttpClient,
) : OpenAiRepository {

    private val _state = MutableSharedFlow<SSEEvent>()
    override val state = _state.asSharedFlow()

    private val gson: Gson = Gson()
    private var eventSource: EventSource? = null

    override suspend fun postCompletions(gpt35Turbo: GPT35Turbo) {
        connectCompletions(gpt35Turbo).collect {
            _state.emit(it)
        }
    }

    private suspend fun connectCompletions(gpt35Turbo: GPT35Turbo): Flow<SSEEvent> {
        val requestBody = gson.toJson(gpt35Turbo)

        val request = requestBuilder
            .post(requestBody.toRequestBody("application/json; charset=UTF-8".toMediaTypeOrNull()))
            .build()

        return callbackFlow {
            val listener = object : EventSourceListener() {

                override fun onOpen(eventSource: EventSource, response: Response) {
                    super.onOpen(eventSource, response)

                    println("Open")
                    trySend(SSEEvent.Open)
                }

                override fun onEvent(
                    eventSource: EventSource,
                    id: String?,
                    type: String?,
                    data: String
                ) {
                    super.onEvent(eventSource, id, type, data)

                    if (data != "[DONE]") {
                        val response = gson.fromJson(data, GPT35TurboResponse::class.java)
                        trySend(SSEEvent.Event(response))
                    }
                }

                override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                    super.onFailure(eventSource, t, response)

                    println("Failure")
                    if (t != null) {
                        trySend(SSEEvent.Failure(t, response))
                    }
                }

                override fun onClosed(eventSource: EventSource) {
                    super.onClosed(eventSource)

                    println("Closed")
                    trySend(SSEEvent.Closed)
                }
            }

            eventSource = EventSources.createFactory(client)
                .newEventSource(request, listener)

            awaitClose { eventSource?.cancel() }

        }.cancellable()
    }

    override fun cancelCompletions() {
        eventSource?.cancel()
    }
}