package ru.wtrn.budgetanalyzer.service

import org.springframework.stereotype.Service
import ru.wtrn.budgetanalyzer.model.Amount
import ru.wtrn.budgetanalyzer.repository.CurrentLimitRepository

@Service
class LimitsService(
    private val currentLimitRepository: CurrentLimitRepository
) {
    suspend fun decreaseLimit(amount: Amount) {
        val foundLimits = currentLimitRepository.findActiveLimits(
            tag = LIMIT_TAG,
            currency = amount.currency
        ).associateBy { it.timespan }
    }

    companion object {
        private const val LIMIT_TAG = "Daily"
    }
}
