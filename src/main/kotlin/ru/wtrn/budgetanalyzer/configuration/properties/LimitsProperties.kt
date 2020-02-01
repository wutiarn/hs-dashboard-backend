package ru.wtrn.budgetanalyzer.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.math.BigDecimal
import java.time.ZoneId
import java.util.Currency
import java.util.TimeZone
import java.util.UUID

@ConstructorBinding
@ConfigurationProperties("budget-analyzer.limits")
data class LimitsProperties(
    val daily: BigDecimal,

    val currency: Currency = Currency.getInstance("RUB"),
    val timezone: ZoneId = ZoneId.of("Europe/Moscow")
)
