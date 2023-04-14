package com.example.feature.openai.state

import okhttp3.Response

sealed interface State {
    object Empty: State
    object Open : State
    data class Event(val response: String) : State
    data class Failure(val e: Throwable, val response: Response?) : State
    object Closed : State
}