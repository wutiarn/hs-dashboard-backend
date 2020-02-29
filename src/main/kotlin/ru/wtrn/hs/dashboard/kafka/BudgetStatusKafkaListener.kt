package ru.wtrn.hs.dashboard.kafka

import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import ru.wtrn.hs.dashboard.controller.BudgetEventsController
import ru.wtrn.hs.dashboard.dto.front.BudgetDto
import ru.wtrn.hs.dashboard.dto.kafka.BudgetStatusKafkaDto
import ru.wtrn.hs.dashboard.entity.LatestEventStateEntity
import ru.wtrn.hs.dashboard.repository.CurrentLimitRepository

@Component
class BudgetStatusKafkaListener(
    private val budgetEventsController: BudgetEventsController,
    private val currentLimitRepository: CurrentLimitRepository
) {
    private val logger = KotlinLogging.logger { }

    @KafkaListener(topics = ["ru.wtrn.hs.dashboard.budget"])
    fun handleBudgetStatus(
        @Payload request: BudgetStatusKafkaDto
    ) = runBlocking {
        logger.info { request }

        val data = BudgetDto(
            balance = request.balance.toString(),
            tomorrow = request.tomorrow.toString(),
            today = request.today.toString(),
            month = request.month.toString(),
            cardBalance = request.cardBalance.toString()
        )

        currentLimitRepository.upsert(
            type = LatestEventStateEntity.EventType.BUDGET,
            data = data
        )

        budgetEventsController.channel.sendBlocking(data)
    }
}
