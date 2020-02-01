package ru.wtrn.budgetanalyzer.service

import org.springframework.stereotype.Service
import ru.wtrn.budgetanalyzer.configuration.properties.LimitsProperties
import ru.wtrn.budgetanalyzer.entity.CurrentLimitEntity
import ru.wtrn.budgetanalyzer.model.Amount
import ru.wtrn.budgetanalyzer.repository.CurrentLimitRepository
import ru.wtrn.budgetanalyzer.util.atEndOfMonth
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class LimitsService(
    private val currentLimitRepository: CurrentLimitRepository,
    private val limitsProperties: LimitsProperties
) {
    suspend fun decreaseLimit(amount: Amount) {
        val foundLimits = currentLimitRepository.findActiveLimits(
            tag = LIMIT_TAG,
            currency = amount.currency
        ).associateBy { it.timespan }

        val monthLimit = foundLimits[CurrentLimitEntity.LimitTimespan.MONTH] ?: constructMonthLimit()
        val dayLimit = foundLimits[CurrentLimitEntity.LimitTimespan.DAY] ?: constructDayLimit(monthLimit)

    }

    private suspend fun constructMonthLimit(): CurrentLimitEntity {
        val tag = LIMIT_TAG
        val timezone = limitsProperties.timezone
        val limitValue = limitsProperties.daily
        val currency = limitsProperties.currency

        val periodStart = LocalDate.now(timezone)
            .withDayOfMonth(1)

        val validUntil = let {
            val endOfMonth = periodStart.atEndOfMonth()
            LocalDateTime.of(endOfMonth, endOfTheDayTime).atZone(timezone).toInstant()
        }

        val entity = CurrentLimitEntity(
            tag = tag,
            periodStart = periodStart,
            spentAmount = Amount(
                value = BigDecimal.ZERO,
                currency = currency
            ),
            limitAmount = Amount(
                value = limitValue,
                currency = currency
            ),
            timespan = CurrentLimitEntity.LimitTimespan.MONTH,
            validUntil = validUntil
        )
        currentLimitRepository.insert(entity)
        return entity
    }

    private suspend fun constructDayLimit(monthLimit: CurrentLimitEntity): CurrentLimitEntity {
        val tag = LIMIT_TAG
        val timezone = limitsProperties.timezone
        val today = LocalDate.now(timezone)
        val validUntil = LocalDateTime.of(today, endOfTheDayTime).atZone(timezone).toInstant()
        val currency = limitsProperties.currency

        val daysRemaining = monthLimit.periodStart.atEndOfMonth().dayOfMonth - today.dayOfMonth + 1

        val limitValue = (monthLimit.limitAmount.value - monthLimit.spentAmount.value) / BigDecimal(daysRemaining)

        val entity = CurrentLimitEntity(
            tag = tag,
            periodStart = today,
            spentAmount = Amount(
                value = BigDecimal.ZERO,
                currency = monthLimit.limitAmount.currency
            ),
            limitAmount = Amount(
                value = limitValue,
                currency = currency
            ),
            validUntil = validUntil,
            timespan = CurrentLimitEntity.LimitTimespan.DAY
        )
        currentLimitRepository.insert(entity)
        return entity
    }

    companion object {
        private const val LIMIT_TAG = "Daily"
        private val endOfTheDayTime = LocalTime.of(23, 59, 59, 999_999_999)
    }
}
