package ru.wtrn.telegram.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class SendMessageRequest(
    @JsonProperty("chat_id")
    val chatId: Int,
    val text: String
)
