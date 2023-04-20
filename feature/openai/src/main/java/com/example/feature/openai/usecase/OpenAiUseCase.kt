package com.example.feature.openai.usecase

import com.example.feature.openai.model.GPT35Turbo
import com.example.feature.openai.repository.OpenAiRepository
import com.example.feature.openai.state.SSEEvent
import com.example.feature.openai.state.State
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

interface OpenAiUseCase {
    suspend fun postCompletions(gpt35Turbo: GPT35Turbo)
    fun cancelCompletions()
    val state: SharedFlow<State>
}

@Singleton
class OpenAiUseCaseImpl @Inject constructor(
    private val repository: OpenAiRepository
) : OpenAiUseCase {

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private val _state = MutableSharedFlow<State>()
    override val state = _state.asSharedFlow()

    init {
        scope.launch {
            repository.state.collect { event ->
                when (event) {
                    is SSEEvent.Empty -> _state.emit(State.Empty)
                    is SSEEvent.Open -> _state.emit(State.Open)
                    is SSEEvent.Event -> {
                        val value = event.response.choices.first().delta.content ?: ""
                        _state.emit(State.Event(value))
                    }
                    is SSEEvent.Failure -> {
                        _state.emit(State.Failure(event.e, event.response))
                    }
                    is SSEEvent.Closed -> _state.emit(State.Closed)
                }
            }
        }
    }
    override suspend fun postCompletions(gpt35Turbo: GPT35Turbo) {
        repository.postCompletions(gpt35Turbo)
    }

    override fun cancelCompletions() {
        repository.cancelCompletions()
    }

}