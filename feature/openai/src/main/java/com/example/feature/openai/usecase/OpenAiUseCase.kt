package com.example.feature.openai.usecase

import com.example.feature.openai.model.GPT35Turbo
import com.example.feature.openai.repository.OpenAiRepository
import com.example.feature.openai.state.SSEEvent
import com.example.feature.openai.state.State
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

interface OpenAiUseCase {
    suspend fun postCompletions(gpt35Turbo: GPT35Turbo)
    fun cancelCompletions()
    val state: StateFlow<State>
}

@Singleton
class OpenAiUseCaseImpl @Inject constructor(
    private val repository: OpenAiRepository
) : OpenAiUseCase {

    private val _state = MutableStateFlow<State>(State.Empty)
    override val state = _state.asStateFlow()

    override suspend fun postCompletions(gpt35Turbo: GPT35Turbo) {
        repository.postCompletions(gpt35Turbo)
        repository.state.collect { event ->

            when (event) {
                is SSEEvent.Empty -> _state.value = State.Empty
                is SSEEvent.Open -> _state.value = State.Open
                is SSEEvent.Event -> {
                    val value = event.response.choices.first().delta.content ?: ""
                    _state.value = State.Event(value)
                }
                is SSEEvent.Failure -> {
                    _state.value = State.Failure(event.e, event.response)
                }
                is SSEEvent.Closed -> _state.value = State.Closed
            }
        }
    }

    override fun cancelCompletions() {
        repository.cancelCompletions()
    }

}