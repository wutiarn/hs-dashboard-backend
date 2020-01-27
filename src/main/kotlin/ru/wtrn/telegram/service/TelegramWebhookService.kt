package ru.wtrn.telegram.service

import org.springframework.stereotype.Service
import ru.wtrn.telegram.configuration.TelegramConfiguration
import ru.wtrn.telegram.configuration.properties.TelegramProperties
import ru.wtrn.telegram.exception.IncorrectWebhookKeyException

@Service
class TelegramWebhookService(
    private val telegramProperties: TelegramProperties
) {
    fun handleWebhook(
        webhookKey: String,
        requestBody: String
    ) {
        if (webhookKey != telegramProperties.webhookKey) {
            throw IncorrectWebhookKeyException()
        }
    }
}
