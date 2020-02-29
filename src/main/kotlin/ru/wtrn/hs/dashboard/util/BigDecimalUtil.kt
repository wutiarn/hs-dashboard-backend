package ru.wtrn.hs.dashboard.util

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private val localizedFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale("ru")))

fun BigDecimal.toLocalizedString(): String {
    return localizedFormat.format(this)
}
