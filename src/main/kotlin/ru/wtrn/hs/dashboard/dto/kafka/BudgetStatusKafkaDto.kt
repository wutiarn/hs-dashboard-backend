package ru.wtrn.hs.dashboard.dto.kafka

import java.math.BigDecimal

class BudgetStatusKafkaDto(
    val balance: BigDecimal,
    val today: BigDecimal,
    val tomorrow: BigDecimal
)
