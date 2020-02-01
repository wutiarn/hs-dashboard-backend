package ru.wtrn.budgetanalyzer.entity

import org.springframework.data.relational.core.mapping.Table
import ru.wtrn.budgetanalyzer.model.Amount
import ru.wtrn.budgetanalyzer.service.LimitsService
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
    val spentValue: BigDecimal,
    val limitValue: BigDecimal,
    val currency: Currency,

    val tag: String,
    val timespan: LimitTimespan,
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
            tag: String,
            timezone: ZoneId,
            limitAmount: Amount
        ): CurrentLimitEntity {
            val periodStart = LocalDate.now(timezone)
                .withDayOfMonth(1)

            val validUntil = let {
                val endOfMonth = periodStart.atEndOfMonth()
                        LocalDateTime.of(endOfMonth, endOfTheDayTime).atZone(timezone).toInstant()
            }

            return CurrentLimitEntity(
                tag = tag,
                periodStart = periodStart,
                spentValue = BigDecimal.ZERO,
                limitValue = limitAmount.value,
                currency = limitAmount.currency,
                timespan = LimitTimespan.MONTH,
                validUntil = validUntil
            )
        }

        fun constructDayLimit(monthLimit: CurrentLimitEntity, timezone: ZoneId): CurrentLimitEntity {
            val today = LocalDate.now(timezone)
            val validUntil = LocalDateTime.of(today, endOfTheDayTime).atZone(timezone).toInstant()
            val daysRemaining = monthLimit.periodStart.atEndOfMonth().dayOfMonth - today.dayOfMonth + 1

            val limitValue = (monthLimit.limitValue - monthLimit.spentValue) / BigDecimal(daysRemaining)

            return CurrentLimitEntity(
                tag = monthLimit.tag,
                periodStart = today,
                spentValue = BigDecimal.ZERO,
                limitValue = limitValue,
                currency = monthLimit.currency,
                validUntil = validUntil,
                timespan = LimitTimespan.DAY
            )
        }

        private val endOfTheDayTime = LocalTime.of(23, 59, 59, 999_999_999)
    }
}
