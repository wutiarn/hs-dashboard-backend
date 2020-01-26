package ru.wtrn.budgetanalyzer.parser

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime

internal class MtsBankSmsParserTest {

    private val parser = MtsBankSmsParser()

    private val receivedAt = LocalDateTime.of(2020, 1, 26, 14, 50)

    @Test
    fun parseMessage() {
        val msg = "Oplata *3947; 26.01 13:03; VKUSVILL 1842 2>MOSCOW RU; 188,40 RUB; Limit: 9 225,12 RUB"
        parser.parseMessage(
            message = msg,
            receivedAt = receivedAt
        )
    }

    @Test
    fun parsePositiveBalance() {
        val msg = "Oplata *3947; 18.01 09:47; VKUSVILL 0123 4>MOSCOW RU; 89,40 RUB; Ostatok: 20 210,64 RUB; " +
                "Sobstvennye sredstva: 210,64 RUB; Kredit: 20 000,00 RUB"
        parser.parseMessage(
            message = msg,
            receivedAt = receivedAt
        )
    }
}
