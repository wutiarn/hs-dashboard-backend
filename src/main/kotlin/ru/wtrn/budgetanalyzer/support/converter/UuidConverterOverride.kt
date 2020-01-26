package ru.wtrn.budgetanalyzer.support.converter

import java.util.UUID
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter

/**
 * Заставляет UUID оставаться UUID при сохранении в базу.
 *
 * Сделано по аналогии с [org.springframework.data.r2dbc.convert.R2dbcConverters.RowToNumberConverterFactory.LocalDateConverterOverride]
 */
@WritingConverter
class UuidConverterOverride : Converter<UUID, UUID> {
    override fun convert(source: UUID): UUID {
        return source
    }
}
