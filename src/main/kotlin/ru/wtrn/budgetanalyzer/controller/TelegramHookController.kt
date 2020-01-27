package ru.wtrn.budgetanalyzer.controller

import mu.KotlinLogging
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.wtrn.telegram.service.TelegramWebhookService

@RestController
class TelegramHookController(
    private val telegramWebhookService: TelegramWebhookService
) {
    private val logger = KotlinLogging.logger { }

    @PostMapping("bot/{webhookKey}")
    fun handleWebhook(
        @PathVariable webhookKey: String,
        @RequestBody request: String
    ) {
        telegramWebhookService.handleWebhook(
            webhookKey = webhookKey,
            requestBody = request
        )
        logger.info { request }
    }
}
