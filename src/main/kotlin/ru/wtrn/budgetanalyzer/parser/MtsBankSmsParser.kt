package ru.wtrn.budgetanalyzer.parser

import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.Currency

@Component
class MtsBankSmsParser {

    fun parseMessage(message: String): MtsBankPaymentMessagePayload {
        TODO()
    }

    data class MtsBankPaymentMessagePayload(
        val pan: String,
        val timestamp: LocalDateTime,
        val merchant: String,
        val location: String,
        val amount: BigDecimal,
        val currency: Currency,
        val remainingBalance: BigDecimal
    )
}
