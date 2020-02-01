package ru.wtrn.budgetanalyzer.support.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import java.util.Currency

@ReadingConverter
class CurrencyReadingConverter : Converter<String, Currency> {
    override fun convert(source: String): Currency {
        return Currency.getInstance(source)
    }
}
