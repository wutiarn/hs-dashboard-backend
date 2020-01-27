package ru.wtrn.telegram.service

import org.springframework.web.reactive.function.client.WebClient

class TelegramMessageService(
    private val webClient: WebClient
) {
    fun sendMessage(chat: Int, text: String) {

    }
}
