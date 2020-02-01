package ru.wtrn.budgetanalyzer.entity

import org.springframework.data.relational.core.mapping.Table
import ru.wtrn.budgetanalyzer.model.Amount
import ru.wtrn.budgetanalyzer.model.CalculatedDayLimit
import ru.wtrn.budgetanalyzer.util.atEndOfMonth
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Currency
import java.util.UUID

@Table("current_limits")
data class CurrentLimitEntity(
    var spentValue: BigDecimal,
    val limitValue: BigDecimal,
    val currency: Currency,

    val timespan: LimitTimespan,
    val timezone: ZoneId,
    val validUntil: Instant,
    val periodStart: LocalDate,

    val createdAt: Instant = Instant.now(),
    val id: UUID = UUID.randomUUID()
) {
    enum class LimitTimespan {
        DAY,
        MONTH
    }

    companion object {
        fun constructMonthLimit(
            timezone: ZoneId,
            limitAmount: Amount,
            today: LocalDate = LocalDate.now(timezone)
        ): CurrentLimitEntity {
            val periodStart = today
                .withDayOfMonth(1)

            val validUntil = let {
                val endOfMonth = periodStart.atEndOfMonth()
                        LocalDateTime.of(endOfMonth, endOfTheDayTime).atZone(timezone).toInstant()
            }

            return CurrentLimitEntity(
                periodStart = periodStart,
                timezone = timezone,
                spentValue = BigDecimal.ZERO,
                limitValue = limitAmount.value,
                currency = limitAmount.currency,
                timespan = LimitTimespan.MONTH,
                validUntil = validUntil
            )
        }

        fun constructDayLimit(
            monthLimit: CurrentLimitEntity,
            date: LocalDate = LocalDate.now(monthLimit.timezone)
        ): CurrentLimitEntity {
            val validUntil = LocalDateTime.of(date, endOfTheDayTime).atZone(monthLimit.timezone).toInstant()
            val calculatedDayLimit = CalculatedDayLimit.of(
                monthStart = monthLimit.periodStart,
                date = date,
                spentValue = monthLimit.spentValue,
                limitValue = monthLimit.limitValue,
                currency = monthLimit.currency
            )
            return CurrentLimitEntity(
                periodStart = date,
                timezone = monthLimit.timezone,
                spentValue = BigDecimal.ZERO,
                limitValue = calculatedDayLimit.limitAmount.value,
                currency = monthLimit.currency,
                validUntil = validUntil,
                timespan = LimitTimespan.DAY
            )
        }

        private val endOfTheDayTime = LocalTime.of(23, 59, 59, 999_999_999)
    }
}
