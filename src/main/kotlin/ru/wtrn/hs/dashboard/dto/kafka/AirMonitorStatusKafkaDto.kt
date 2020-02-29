package ru.wtrn.hs.dashboard.dto.kafka

import java.math.BigDecimal

data class AirMonitorStatusKafkaDto(
    val co2: Int,
    val humidity: BigDecimal,
    val pm25: BigDecimal,
    val temperature: BigDecimal,
    val tvoc: Int
)
