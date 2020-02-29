package ru.wtrn.hs.dashboard.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mu.KotlinLogging
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController
import ru.wtrn.hs.dashboard.dto.TimestampEventDtp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RestController
class TimestampEventsController(
    private val objectMapper: ObjectMapper
) {
    private val logger = KotlinLogging.logger { }

    @MessageMapping("events.timestamp")
    fun requestEvents(): Flow<TimestampEventDtp> = flow {
        var counter = 1;
        while (true) {
//            if (counter > 30) {
//                return@flow
//            }
            val now = LocalDateTime.now(ZoneId.of("Europe/Moscow"))

            val locale = Locale.ENGLISH

            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd", locale)

            val timeStr = now.format(timeFormatter)
            val dateStr = now.format(dateFormatter)

            val nowStr = "$timeStr $dateStr"
            val event = TimestampEventDtp(
                counter = counter++,
                timestamp = nowStr
            )
            emit(event)
            delay(1000)
        }
    }
}
