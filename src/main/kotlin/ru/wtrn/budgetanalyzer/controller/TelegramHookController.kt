package ru.wtrn.budgetanalyzer.controller

import mu.KotlinLogging
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TelegramHookController {
    private val logger = KotlinLogging.logger {  }

    @PostMapping("bot/{webhookKey}")
    fun handleWebhook(
        @PathVariable webhookKey: String,
        @RequestBody request: String
    ) {
        logger.info { request }
    }
}
