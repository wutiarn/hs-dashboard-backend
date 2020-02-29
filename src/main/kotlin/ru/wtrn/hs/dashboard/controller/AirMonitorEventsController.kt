package ru.wtrn.hs.dashboard.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mu.KotlinLogging
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController
import ru.wtrn.hs.dashboard.dto.front.AirMonitorStatusDto
import ru.wtrn.hs.dashboard.dto.front.BudgetDto
import ru.wtrn.hs.dashboard.entity.LatestEventStateEntity
import ru.wtrn.hs.dashboard.repository.LatestEventStateRepository

@Suppress("EXPERIMENTAL_API_USAGE")
@RestController
class AirMonitorEventsController(
    private val objectMapper: ObjectMapper,
    private val latestEventStateRepository: LatestEventStateRepository
) {
    private val logger = KotlinLogging.logger { }

    val channel = BroadcastChannel<AirMonitorStatusDto>(1)

    @MessageMapping("events.airMonitor")
    fun requestEvents(): Flow<AirMonitorStatusDto> = flow {
        latestEventStateRepository.retrieve(
            type = LatestEventStateEntity.EventType.AIR_MONITOR,
            cls = AirMonitorStatusDto::class.java
        )?.let { initialEvent ->
            emit(initialEvent)
        }

        channel.openSubscription().consumeEach {
            emit(it)
        }
    }
}
