package ru.wtrn.budgetanalyzer.controller

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.wtrn.telegram.service.TelegramWebhookService
import javax.xml.bind.JAXBElement

@RestController
class TelegramHookController(
    private val telegramWebhookService: TelegramWebhookService
) {
    private val logger = KotlinLogging.logger { }

    @PostMapping("bot/{webhookKey}")
    suspend fun handleWebhook(
        @PathVariable webhookKey: String,
        @RequestBody request: String
    ) {
        logger.info { request }
        telegramWebhookService.handleWebhook(
            webhookKey = webhookKey,
            requestBody = request
        )
    }
}
