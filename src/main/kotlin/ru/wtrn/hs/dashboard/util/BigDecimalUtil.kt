package ru.wtrn.hs.dashboard.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private val localizedFormat = DecimalFormat.getInstance(Locale("ru"))

fun BigDecimal.toLocalizedString(scale: Int = 0, roundingMode: RoundingMode = RoundingMode.DOWN): String {
    val scaled = this.setScale(scale, roundingMode)
    return localizedFormat.format(scaled)
}
