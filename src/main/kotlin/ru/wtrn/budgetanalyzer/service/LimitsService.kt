package ru.wtrn.budgetanalyzer.service

import org.springframework.stereotype.Service
import ru.wtrn.budgetanalyzer.configuration.properties.LimitsProperties
import ru.wtrn.budgetanalyzer.entity.CurrentLimitEntity
import ru.wtrn.budgetanalyzer.model.Amount
import ru.wtrn.budgetanalyzer.repository.CurrentLimitRepository

@Service
class LimitsService(
    private val currentLimitRepository: CurrentLimitRepository,
    private val limitsProperties: LimitsProperties
) {
    suspend fun decreaseLimit(amount: Amount): RemainingLimit {
        val foundLimits = currentLimitRepository.findActiveLimits(
            tag = LIMIT_TAG,
            currency = amount.currency
        ).associateBy { it.timespan }

        val monthLimit = foundLimits[CurrentLimitEntity.LimitTimespan.MONTH] ?: constructMonthLimit()
        val dayLimit = foundLimits[CurrentLimitEntity.LimitTimespan.DAY] ?: constructDayLimit(monthLimit)

        currentLimitRepository.increaseSpentAmount(
            limitIds = listOf(monthLimit.id, dayLimit.id),
            amountValue = amount.value
        )

        return RemainingLimit(
            day = calculateRemainingAmount(dayLimit, amount),
            month = calculateRemainingAmount(monthLimit, amount)
        )
    }

    private fun calculateRemainingAmount(
        limit: CurrentLimitEntity,
        transactionAmount: Amount
    ): Amount {
        return Amount(
            value = limit.limitAmount.value - (limit.spentAmount.value + transactionAmount.value),
            currency = limit.limitAmount.currency
        )
    }

    private suspend fun constructMonthLimit(): CurrentLimitEntity {
        val entity = CurrentLimitEntity.constructMonthLimit(
            tag = LIMIT_TAG,
            limitAmount = Amount(
                value = limitsProperties.daily,
                currency = limitsProperties.currency
            ),
            timezone = limitsProperties.timezone
        )
        currentLimitRepository.insert(entity)
        return entity
    }

    private suspend fun constructDayLimit(monthLimit: CurrentLimitEntity): CurrentLimitEntity {
        val entity = CurrentLimitEntity.constructDayLimit(
            monthLimit = monthLimit,
            timezone = limitsProperties.timezone
        )
        currentLimitRepository.insert(entity)
        return entity
    }

    companion object {
        private const val LIMIT_TAG = "Daily"
    }

    data class RemainingLimit(
        val day: Amount,
        val month: Amount
    )
}
