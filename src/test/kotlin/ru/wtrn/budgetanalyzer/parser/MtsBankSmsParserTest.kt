package ru.wtrn.budgetanalyzer.parser

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.wtrn.budgetanalyzer.model.Amount
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Currency

internal class MtsBankSmsParserTest {

    private val parser = MtsBankSmsParser()

    private val receivedAt = LocalDateTime.of(2020, 1, 26, 14, 50)

    @Test
    fun parseMessage() {
        val msg = "Oplata *3947; 26.01 13:03; VKUSVILL 0123 2>MOSCOW RU; 188,40 RUB; Limit: 9 225,12 RUB"
        val parsed = parser.parseMessage(
            message = msg,
            receivedAt = receivedAt
        )
        expectThat(parsed).isEqualTo(
            MtsBankSmsParser.MtsBankPaymentMessagePayload(
                panSuffix = "3947",
                timestamp = LocalDateTime.of(2020, 1, 26, 13, 3),
                merchant = "VKUSVILL 0123 2",
                location = "MOSCOW RU",
                amount = Amount(
                    value = BigDecimal("188.40"),
                    currency = Currency.getInstance("RUB")
                ),
                remainingBalance = Amount(
                    value = BigDecimal("9225.12"),
                    currency = Currency.getInstance("RUB")
                )
            )
        )
    }

    @Test
    fun parsePositiveBalance() {
        val msg = "Oplata *3947; 18.01 09:47; VKUSVILL 0123 4>MOSCOW RU; 89,40 RUB; Ostatok: 20 210,64 RUB; " +
                "Sobstvennye sredstva: 210,64 RUB; Kredit: 20 000,00 RUB"
        val parsed = parser.parseMessage(
            message = msg,
            receivedAt = receivedAt
        )
        expectThat(parsed).isEqualTo(
            MtsBankSmsParser.MtsBankPaymentMessagePayload(
                panSuffix = "3947",
                timestamp = LocalDateTime.of(2020, 1, 18, 9, 47),
                merchant = "VKUSVILL 0123 4",
                location = "MOSCOW RU",
                amount = Amount(
                    value = BigDecimal("89.40"),
                    currency = Currency.getInstance("RUB")
                ),
                remainingBalance = Amount(
                    value = BigDecimal("20210.64"),
                    currency = Currency.getInstance("RUB")
                )
            )
        )
    }
}
