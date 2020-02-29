package ru.wtrn.hs.dashboard.kafka

import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import ru.wtrn.hs.dashboard.dto.kafka.BudgetStatusKafkaDto

@Component
class BudgetStatusKafkaListener {
    fun handleBudgetStatus(
        @Payload request: BudgetStatusKafkaDto
    ) {

    }
}
