package ru.wtrn.budgetanalyzer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@RestController
@RequestMapping("hooks/sms")
class SmsHookController(
    private val objectMapper: ObjectMapper
) {
    private val logger = KotlinLogging.logger { }
    private val delimiter = "^~"
    @PostMapping
    fun handleSms(@RequestBody dataStr: String): String {
        val payload = try {
            parsePayload(dataStr)
        } catch (e: Exception) {
            logger.warn { "Failed to parse received sms payload: '$dataStr'" }
            throw e
        }
        logger.info { "Received sms event: ${objectMapper.writeValueAsString(payload)}" }
        return "OK"
    }

    private fun parsePayload(dataStr: String): SmsData {
        val parts = dataStr.split(delimiter)
        val date = parts[3]
        val time = parts[4]
        val timestamp = LocalDateTime.parse("$date $time", DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm"))
        return SmsData(
            secret = UUID.fromString(parts[0]),
            from = parts[1],
            timestamp = timestamp,
            body = parts[2]
        )
    }

    data class SmsData(
        val secret: UUID,
        val from: String,
        val timestamp: LocalDateTime,
        val body: String
    )
}
