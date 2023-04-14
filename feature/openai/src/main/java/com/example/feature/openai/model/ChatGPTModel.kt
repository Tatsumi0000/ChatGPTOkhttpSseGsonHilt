package com.example.feature.openai.model

import com.google.gson.annotations.SerializedName

data class GPT35Turbo(
    val model: Model = Model.GPT35TURBO,
    val messages: List<Messages>,
    val temperature: Double = 0.7,
    val stream: Boolean = true
)

enum class Model {
    @SerializedName("gpt-3.5-turbo")
    GPT35TURBO
}

data class Messages(
    val role: Role,
    val content: String
) {
    enum class Role {
        @SerializedName("system")
        SYSTEM,

        @SerializedName("system")
        USER,

        @SerializedName("assistant")
        ASSISTANT
    }
}

data class GPT35TurboResponse(
    val id: String,
    @SerializedName("object")
    val object_: String,
    val created: Int,
    val model: Model,
    val usage: Usage,
    val choices: List<Choices>
) {
    data class Usage(
        @SerializedName("prompt_tokens")
        val promptTokens: Int,
        @SerializedName("completion_tokens")
        val completionTokens: Int,
        @SerializedName("total_tokens")
        val totalTokens: Int
    )

    data class Choices(
        val message: Messages,
        @SerializedName("finish_reason")
        val finishReason: String,
        val delta: Delta
    )
    data class Delta(
        val content: String
    )
}