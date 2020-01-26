package ru.wtrn.budgetanalyzer.support.converter

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.r2dbc.postgresql.codec.Json
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter
import org.springframework.data.convert.WritingConverter

@WritingConverter
class JsonbWritingConverter(
    private val objectMapper: ObjectMapper
) : GenericConverter {
    override fun getConvertibleTypes(): MutableSet<GenericConverter.ConvertiblePair>? {
        return mutableSetOf(
            GenericConverter.ConvertiblePair(PgJsonSerializable::class.java, Json::class.java),
            GenericConverter.ConvertiblePair(JsonNode::class.java, Json::class.java)
        )
    }

    override fun convert(source: Any?, sourceType: TypeDescriptor, targetType: TypeDescriptor): Any? {
        return toJson(source)
    }

    fun toJson(source: Any?): Json {
        return Json.of(objectMapper.writeValueAsString(source))
    }
}
