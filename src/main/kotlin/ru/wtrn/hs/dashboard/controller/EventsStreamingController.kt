package ru.wtrn.hs.dashboard.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mu.KotlinLogging
import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController
import ru.wtrn.hs.dashboard.dto.FrontendEventDto
import java.time.LocalDateTime
import java.time.ZoneId

@RestController
class EventsStreamingController(
    private val objectMapper: ObjectMapper
) {
    private val logger = KotlinLogging.logger { }

    @MessageMapping("events")
    fun requestEvents(): Flow<FrontendEventDto> = flow {
        while (true) {
            val event = FrontendEventDto(
                timestamp = LocalDateTime.now(ZoneId.of("Europe/Moscow")).toString()
            )
            logger.info { "Emitting $event" }
            emit(event)
            delay(1000)
        }
    }
}
