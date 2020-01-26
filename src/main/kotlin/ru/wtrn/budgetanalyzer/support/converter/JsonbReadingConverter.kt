package ru.wtrn.budgetanalyzer.support.converter

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.r2dbc.postgresql.codec.Json
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter
import org.springframework.data.convert.ReadingConverter

@ReadingConverter
class JsonbReadingConverter(
    private val objectMapper: ObjectMapper
) : GenericConverter {
    override fun getConvertibleTypes(): MutableSet<GenericConverter.ConvertiblePair>? {
        return mutableSetOf(
            GenericConverter.ConvertiblePair(Json::class.java, PgJsonSerializable::class.java),
            GenericConverter.ConvertiblePair(Json::class.java, JsonNode::class.java)
        )
    }

    override fun convert(source: Any?, sourceType: TypeDescriptor, targetType: TypeDescriptor): Any? {
        val json = source as? Json ?: throw IllegalStateException("Source type must be Json")
        return objectMapper.readValue(json.asArray(), targetType.objectType)
    }
}
