package ru.wtrn.budgetanalyzer.util

import java.time.LocalDate
import java.util.Date

fun LocalDate.atEndOfMonth() = this.withDayOfMonth(1).plusMonths(1).minusDays(1)
