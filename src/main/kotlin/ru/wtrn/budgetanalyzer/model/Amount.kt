package ru.wtrn.budgetanalyzer.model

import ru.wtrn.budgetanalyzer.support.converter.PgJsonSerializable
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

data class Amount(
    val value: BigDecimal,
    val currency: Currency
) : PgJsonSerializable {

    override fun toString(): String {
        val formattedValue = numberFormat.format(value).dropLast(2)
        return "$formattedValue ${currency.currencyCode}"
    }

    companion object {
        private val amountRegex = "(?<amount>[\\d, ]+) (?<currency>\\w{3})".toRegex()
        private val numberFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("ru"))

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
