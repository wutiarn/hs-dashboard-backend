package ru.wtrn.budgetanalyzer.model

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Currency

internal class CalculatedDayLimitTest {
    @Test
    fun testEndOfMonth() {
        val date = LocalDate.of(2020, 2, 29)
        val result = CalculatedDayLimit.of(
            date = date,
            spentValue = BigDecimal.valueOf(5_000),
            limitValue = BigDecimal.valueOf(40_000),
            currency = Currency.getInstance("RUB")
        )

        expectThat(result).isEqualTo(
            CalculatedDayLimit(
                daysRemaining = 1,
                limitAmount = Amount(
                    BigDecimal.valueOf(35_000),
                    Currency.getInstance("RUB")
                )
            )
        )
    }

    @Test
    fun testStartOfMonth() {
        val date = LocalDate.of(2020, 2, 1)
        val result = CalculatedDayLimit.of(
            date = date,
            spentValue = BigDecimal.ZERO,
            limitValue = BigDecimal.valueOf(40_000),
            currency = Currency.getInstance("RUB")
        )

        expectThat(result).isEqualTo(
            CalculatedDayLimit(
                daysRemaining = 29,
                limitAmount = Amount(
                    BigDecimal.valueOf(1379),
                    Currency.getInstance("RUB")
                )
            )
        )
    }

    @Test
    fun testMiddleOfMonth() {
        val date = LocalDate.of(2020, 1, 15)
        val result = CalculatedDayLimit.of(
            date = date,
            spentValue = BigDecimal.valueOf(7_000.21),
            limitValue = BigDecimal.valueOf(40_000),
            currency = Currency.getInstance("RUB")
        )

        expectThat(result).isEqualTo(
            CalculatedDayLimit(
                daysRemaining = 17,
                limitAmount = Amount(
                    BigDecimal.valueOf(1941.16),
                    Currency.getInstance("RUB")
                )
            )
        )
    }
}
