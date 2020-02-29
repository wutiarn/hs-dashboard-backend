package ru.wtrn.hs.dashboard.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mu.KotlinLogging
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController
import ru.wtrn.hs.dashboard.dto.AirMonitorStatusDto
import ru.wtrn.hs.dashboard.dto.TimestampEventDtp
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@RestController
class AirMonitorEventsController(
    private val objectMapper: ObjectMapper
) {
    private val logger = KotlinLogging.logger { }

    @MessageMapping("events.airMonitor")
    fun requestEvents(): Flow<AirMonitorStatusDto> = flow {
        while (true) {
            val event = AirMonitorStatusDto(
                co2 = "1159",
                temperature = "26.1",
                rh = "28.5",
                pm25 = "7.1",
                tvoc = "280"
            )
            emit(event)
            delay(1000)
        }
    }
}
