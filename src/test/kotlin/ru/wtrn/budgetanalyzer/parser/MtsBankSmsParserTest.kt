package ru.wtrn.budgetanalyzer.parser

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MtsBankSmsParserTest {

    val parser = MtsBankSmsParser()

    @Test
    fun parseMessage() {
        val result = parser.parseMessage("Oplata *3947; 26.01 13:03; VKUSVILL 1842 2>MOSCOW RU; 188,40 RUB; Limit: 9 225,12 RUB")
    }
}
