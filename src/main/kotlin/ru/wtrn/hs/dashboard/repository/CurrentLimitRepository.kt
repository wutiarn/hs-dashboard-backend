package ru.wtrn.hs.dashboard.repository

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.await
import org.springframework.data.r2dbc.core.awaitFirstOrNull
import org.springframework.stereotype.Repository
import ru.wtrn.hs.dashboard.entity.LatestEventStateEntity
import ru.wtrn.hs.dashboard.support.CoroutineCrudRepository
import java.time.Instant
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
        databaseClient.execute(
            //language=PostgreSQL
            """
                SELECT *
                FROM latest_event_states
                WHERE type = :type
            """.trimIndent()
        )
            .bind("type", type.name)
            .`as`(domainType)
            .fetch()
            .awaitFirstOrNull()
            ?.let {
                @Suppress("BlockingMethodInNonBlockingContext")
                objectMapper.treeToValue(it.data, cls)
            }
}
