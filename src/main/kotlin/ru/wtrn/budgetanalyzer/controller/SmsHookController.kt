package ru.wtrn.budgetanalyzer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.wtrn.budgetanalyzer.model.SmsMessage
import ru.wtrn.budgetanalyzer.service.SmsMessageRoutingService

@RestController
@RequestMapping("hooks/sms")
class SmsHookController(
    private val objectMapper: ObjectMapper,
    private val routingService: SmsMessageRoutingService
) {
    private val logger = KotlinLogging.logger { }
    @PostMapping
    suspend fun handleSms(@RequestBody dataStr: String): String {
        val message = try {
            SmsMessage.parse(dataStr)
        } catch (e: Exception) {
            logger.warn { "Failed to parse received sms payload: '$dataStr'" }
            throw e
        }
        logger.info { "Received sms event: ${objectMapper.writeValueAsString(message)}" }
        routingService.handleMessage(message)
        return "OK"
    }
}
