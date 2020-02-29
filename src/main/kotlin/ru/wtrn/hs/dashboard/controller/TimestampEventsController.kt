package ru.wtrn.hs.dashboard.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mu.KotlinLogging
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController
import ru.wtrn.hs.dashboard.dto.front.TimestampEventDto
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@RestController
class TimestampEventsController(
    private val objectMapper: ObjectMapper
) {
    private val logger = KotlinLogging.logger { }

    @MessageMapping("events.timestamp")
    fun requestEvents(): Flow<TimestampEventDto> = flow {
        while (true) {
            val now = LocalDateTime.now(ZoneId.of("Europe/Moscow"))

            val locale = Locale("ru")

            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM, EEEE", locale)

            val timeStr = now.format(timeFormatter)
            val dateStr = now.format(dateFormatter)

            val event = TimestampEventDto(
                time = timeStr,
                date = dateStr
            )
            emit(event)
            sleepUntilUnitChanges(now, ChronoUnit.MINUTES)
        }
    }

    private suspend fun sleepUntilUnitChanges(now: LocalDateTime, unit: ChronoUnit) {
        val until = now.plus(1, unit).truncatedTo(unit)
        val sleepTime = Duration.between(now, until)
        delay(sleepTime.toMillis())
    }
}
