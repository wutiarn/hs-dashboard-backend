package ru.wtrn.hs.dashboard.kafka

import kotlinx.coroutines.channels.sendBlocking
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import ru.wtrn.hs.dashboard.controller.BudgetEventsController
import ru.wtrn.hs.dashboard.dto.front.BudgetDto
import ru.wtrn.hs.dashboard.dto.kafka.BudgetStatusKafkaDto

@Component
class BudgetStatusKafkaListener(
    private val budgetEventsController: BudgetEventsController
) {
    private val logger = KotlinLogging.logger { }

    @KafkaListener(topics = ["ru.wtrn.hs.dashboard.budget"])
    fun handleBudgetStatus(
        @Payload request: BudgetStatusKafkaDto
    ) {
        logger.info { request }
        budgetEventsController.channel.sendBlocking(
            BudgetDto(
                balance = request.balance.toString(),
                tomorrow = request.tomorrow.toString(),
                today = request.today.toString()
            )
        )
    }
}
