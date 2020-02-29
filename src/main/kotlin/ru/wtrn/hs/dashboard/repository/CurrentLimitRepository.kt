package ru.wtrn.hs.dashboard.repository

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.await
import org.springframework.stereotype.Repository
import ru.wtrn.hs.dashboard.entity.LatestEventStateEntity
import ru.wtrn.hs.dashboard.support.CoroutineCrudRepository
import java.util.UUID

@Repository
class CurrentLimitRepository(
    databaseClient: DatabaseClient,
    private val objectMapper: ObjectMapper
) : CoroutineCrudRepository<LatestEventStateEntity, String>(
    domainType = LatestEventStateEntity::class.java,
    idColumn = "type",
    databaseClient = databaseClient
) {
    suspend fun upsert(
        type: LatestEventStateEntity.EventType,
        data: Any
    ): LatestEventStateEntity {
        val jsonNode = objectMapper.valueToTree<JsonNode>(data)
        val entity = LatestEventStateEntity(
            type = type,
            data = jsonNode
        )
        databaseClient.execute(
            //language=PostgreSQL
            """
                DELETE FROM latest_event_states
                WHERE type = :type
            """.trimIndent()
        )
            .bind("type", type.name)
            .await()
        insert(entity)
        return entity
    }

    suspend fun <T> retrieve(type: LatestEventStateEntity.EventType, cls: Class<T>): T? =
        findById(id = type.name)?.let { entity ->
            @Suppress("BlockingMethodInNonBlockingContext")
            objectMapper.treeToValue(entity.data, cls)
        }
}
