package ru.wtrn.budgetanalyzer.parser

import org.springframework.stereotype.Component
import ru.wtrn.budgetanalyzer.entity.TransactionEntity
import ru.wtrn.budgetanalyzer.model.Amount
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField

@Component
class MtsBankSmsParser {
    fun parseForTransaction(message: String, receivedAt: LocalDateTime): TransactionEntity {
        val parsedMessage = parseMessage(message, receivedAt)
        return TransactionEntity(
            bank = TransactionEntity.Bank.MTS_BANK,
            cardPanSuffix = parsedMessage.panSuffix,
            timestamp = parsedMessage.timestamp,
            merchant = parsedMessage.merchant,
            location = parsedMessage.location,
            amount = parsedMessage.amount,
            remainingBalance = parsedMessage.remainingBalance
        )
    }

    @Suppress("SimpleRedundantLet")
    fun parseMessage(message: String, receivedAt: LocalDateTime): MtsBankPaymentMessagePayload {
        // Oplata *3947; 18.01 09:47; VKUSVILL 0123 4>MOSCOW RU; 89,40 RUB; Ostatok: 20 210,64 RUB; Sobstvennye sredstva: 210,64 RUB; Kredit: 20 000,00 RUB
        // Oplata *3947; 26.01 13:03; VKUSVILL 0123 2>MOSCOW RU; 188,40 RUB; Limit: 9 225,12 RUB
        if (!message.startsWith("Oplata")) {
            // Ignore non-transactional messages
            throw IllegalArgumentException("Non-transactional messages are not supported")
        }
        val messageParts = message.split("; ")
        val pan = messageParts[0].let {
            // Oplata *3947
            it.split("*").last()
        }
        val timestamp = messageParts[1].let {
            // 18.01 13:03
            val parsed = DateTimeFormatter.ofPattern("dd.MM HH:mm").parse(it)
            val dayOfMonth = parsed.get(ChronoField.DAY_OF_MONTH)
            val monthOfYear = parsed.get(ChronoField.MONTH_OF_YEAR)
            val year = receivedAt.year
            val hour = parsed.get(ChronoField.HOUR_OF_DAY)
            val minute = parsed.get(ChronoField.MINUTE_OF_HOUR)
            LocalDateTime.of(year, monthOfYear, dayOfMonth, hour, minute)
        }
        val (merchant, location) = messageParts[2].let {
            // VKUSVILL 0123 4>MOSCOW RU
            it.split(">")
        }
        val amount = messageParts[3].let {
            // 89,40 RUB
            Amount.parse(it)
        }
        val remainingBalance = messageParts[4].let {
            // Ostatok: 20 210,64 RUB
            // Limit: 9 225,12 RUB
            val remainingBalanceStr = it.split(": ").last()
            Amount.parse(remainingBalanceStr)
        }
        return MtsBankPaymentMessagePayload(
            panSuffix = pan,
            timestamp = timestamp,
            merchant = merchant,
            location = location,
            amount = amount,
            remainingBalance = remainingBalance
        )
    }

    data class MtsBankPaymentMessagePayload(
        val panSuffix: String,
        val timestamp: LocalDateTime,
        val merchant: String,
        val location: String,
        val amount: Amount,
        val remainingBalance: Amount
    )
}
