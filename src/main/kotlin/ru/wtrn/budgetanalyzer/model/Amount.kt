package ru.wtrn.budgetanalyzer.model

import java.math.BigDecimal
import java.util.Currency

data class Amount(
    val value: BigDecimal,
    val currency: Currency
) {
    companion object {
        private val amountRegex = "(?<amount>[\\d, ]+) (?<currency>\\w{3})".toRegex()

        /**
         * @sample it 89,40 RUB
         */
        fun parse(it: String): Amount {
            val groups = amountRegex.matchEntire(it)?.groups ?: throw IllegalArgumentException("Provided regex doesn't match regex")
            @Suppress("ComplexRedundantLet")
            val amount = groups["amount"]!!.value
                .replace(",", ".")
                .replace(" ", "")
                .let {
                    BigDecimal(it.replace(",", "."))
                }

            val currency = groups["currency"]!!.value.let {
                Currency.getInstance(it)
            }

            return Amount(
                value = amount,
                currency = currency
            )
        }
    }
}
