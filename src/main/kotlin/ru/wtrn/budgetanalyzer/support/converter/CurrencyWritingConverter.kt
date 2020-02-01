package ru.wtrn.budgetanalyzer.support.converter

import java.util.UUID
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import java.util.Currency

@WritingConverter
class CurrencyWritingConverter : Converter<Currency, String> {
    override fun convert(source: Currency): String {
        return source.currencyCode
    }
}
