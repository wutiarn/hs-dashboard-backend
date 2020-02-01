package ru.wtrn.budgetanalyzer.model

import ru.wtrn.budgetanalyzer.util.atEndOfMonth
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Currency


data class CalculatedDayLimit(
    val daysRemaining: Int,
    val limitAmount: Amount
) {
    companion object {
        fun of(
            monthStart: LocalDate,
            date: LocalDate,
            spentValue: BigDecimal,
            limitValue: BigDecimal,
            currency: Currency
        ): CalculatedDayLimit {
            val daysRemaining = monthStart.atEndOfMonth().dayOfMonth - date.dayOfMonth + 1
            val calculatedLimitValue = (limitValue - spentValue) / BigDecimal(daysRemaining)
            return CalculatedDayLimit(
                daysRemaining = daysRemaining,
                limitAmount = Amount(
                    value = calculatedLimitValue,
                    currency = currency
                )
            )
        }
    }
}
