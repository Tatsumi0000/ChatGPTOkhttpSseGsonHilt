package com.example.chatgptretrofitgsonhilt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.openai.model.GPT35Turbo
import com.example.feature.openai.model.Messages
import com.example.feature.openai.state.SSEEvent
import com.example.feature.openai.state.State
import com.example.feature.openai.usecase.OpenAiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val useCase: OpenAiUseCase
) : ViewModel() {

    data class UiState(
        val generatedText: String = "",
    )

    sealed interface UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent
        object Empty : UiEvent
    }

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
    private val _event = Channel<UiEvent>()
    val event: Flow<UiEvent> = _event.consumeAsFlow()

    init {
        viewModelScope.launch {
            useCase.state.collect { state ->
                when (state) {
                    is State.Event -> {
                        _state.update {
                            UiState(it.generatedText + state.response)
                        }
                    }
                    is State.Closed -> {
                        _event.send(UiEvent.ShowSnackBar("完了しました"))
                    }
                    else -> {

                    }
                }
            }
        }
    }

    fun start() {

        viewModelScope.launch {
            _state.update { UiState("") }
            val messages = listOf(
                Messages(Messages.Role.SYSTEM, "あなたは生粋の関西人です。"),
                Messages(Messages.Role.ASSISTANT, "大阪名物について"),
                Messages(Messages.Role.USER, "150文字以内かつ関西弁で紹介して下さい。"),
            )
            val gpt35Turbo = GPT35Turbo(messages = messages)
            useCase.postCompletions(gpt35Turbo)
        }

    }
}