package ru.wtrn.hs.dashboard.entity

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.data.relational.core.mapping.Table

@Table("latest_event_states")
data class LatestEventStateEntity(
    val type: EventType,
    val data: JsonNode
) {
    enum class EventType {
        BUDGET,
        AIR_MONITOR
    }
}
