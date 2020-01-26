package ru.wtrn.budgetanalyzer.model

import ru.wtrn.budgetanalyzer.controller.SmsHookController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class SmsMessage(
    val secret: UUID,
    val from: String,
    val timestamp: LocalDateTime,
    val body: String
) {
    companion object {
        private const val delimiter = "^~"
        fun parse(hookPayload: String): SmsMessage {
            val parts = hookPayload.split(delimiter)
            val date = parts[3]
            val time = parts[4]
            val timestamp = LocalDateTime.parse("$date $time", DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm"))
            return SmsMessage(
                secret = UUID.fromString(parts[0]),
                from = parts[1],
                timestamp = timestamp,
                body = parts[2]
            )
        }
    }
}
