package ru.wtrn.telegram.service

import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitExchange
import ru.wtrn.telegram.dto.request.SendMessageRequest

class TelegramMessageService(
    private val webClient: WebClient
) {
    suspend fun sendMessage(chat: Int, text: String) {
        val request = SendMessageRequest(
            chatId = chat,
            text = text
        )
        webClient.post()
            .uri("sendMessage")
            .bodyValue(request)
            .awaitExchange()
    }
}
