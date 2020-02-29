package ru.wtrn.hs.dashboard.kafka

import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import ru.wtrn.hs.dashboard.controller.AirMonitorEventsController
import ru.wtrn.hs.dashboard.dto.front.AirMonitorStatusDto
import ru.wtrn.hs.dashboard.dto.kafka.AirMonitorStatusKafkaDto
import ru.wtrn.hs.dashboard.entity.LatestEventStateEntity
import ru.wtrn.hs.dashboard.repository.LatestEventStateRepository
import ru.wtrn.hs.dashboard.util.toLocalizedString

@Component
class AirMonitorStatusKafkaListener(
    private val airMonitorEventsController: AirMonitorEventsController,
    private val latestEventStateRepository: LatestEventStateRepository
) {
    private val logger = KotlinLogging.logger { }

    @KafkaListener(topics = ["ru.wtrn.hs.dashboard.air-monitor"])
    fun handleBudgetStatus(
        @Payload request: AirMonitorStatusKafkaDto
    ) = runBlocking {
        logger.info { request }

        val data = AirMonitorStatusDto(
            co2 = request.co2.toLocalizedString(),
            temperature = request.temperature.toLocalizedString(scale = 1),
            pm25 = request.pm25.toLocalizedString(scale = 1),
            tvoc = request.tvoc.toLocalizedString(),
            rh = request.humidity.toLocalizedString(scale = 1)
        )

        latestEventStateRepository.upsert(
            type = LatestEventStateEntity.EventType.AIR_MONITOR,
            data = data
        )

        airMonitorEventsController.channel.sendBlocking(data)
    }
}
