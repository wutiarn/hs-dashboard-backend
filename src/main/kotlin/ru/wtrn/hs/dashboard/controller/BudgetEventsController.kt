package ru.wtrn.hs.dashboard.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mu.KotlinLogging
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController
import ru.wtrn.hs.dashboard.dto.BudgetDto

@RestController
class BudgetEventsController(
    private val objectMapper: ObjectMapper
) {
    private val logger = KotlinLogging.logger { }

    @MessageMapping("events.budget")
    fun requestEvents(): Flow<BudgetDto> = flow {
        while (true) {
            val event = BudgetDto(
                balance = "5 000",
                today = "1 283",
                tomorrow = "1 574"
            )
            emit(event)
            delay(1000)
        }
    }
}
