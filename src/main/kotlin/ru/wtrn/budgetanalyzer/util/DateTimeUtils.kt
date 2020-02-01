package ru.wtrn.budgetanalyzer.util

import java.time.LocalDate
import java.util.Date

fun LocalDate.atEndOfMonth() = this.plusMonths(1).minusDays(1)
