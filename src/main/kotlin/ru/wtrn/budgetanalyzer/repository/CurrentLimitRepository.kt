package ru.wtrn.budgetanalyzer.repository

import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.await
import org.springframework.stereotype.Repository
import ru.wtrn.budgetanalyzer.entity.CurrentLimitEntity
import ru.wtrn.budgetanalyzer.support.CoroutineCrudRepository
import java.math.BigDecimal
import java.sql.Date
import java.time.Instant
import java.util.Currency
import java.util.UUID

@Repository
class CurrentLimitRepository(
    databaseClient: DatabaseClient
) : CoroutineCrudRepository<CurrentLimitEntity, UUID>(
    domainType = CurrentLimitEntity::class.java,
    idColumn = "id",
    databaseClient = databaseClient
) {
    suspend fun findActiveLimits(tag: String, currency: Currency): List<CurrentLimitEntity> {
        return databaseClient.execute(
            //language=PostgreSQL
            """
                SELECT *
                FROM current_limits
                WHERE tag = :tag 
                AND valid_until > :validUntilGt
            """.trimIndent()
        )
            .bind("tag", tag)
            .bind("validUntilGt", Instant.now())
            .`as`(domainType)
            .fetch()
            .all()
            .collectList()
            .awaitSingle()
    }

    suspend fun increaseSpentAmount(limitIds: List<UUID>, amountValue: BigDecimal) {
        return databaseClient.execute(
            //language=PostgreSQL
            """
                UPDATE current_limits
                SET spent_value = spent_value + :amountValue
                WHERE id in (:limitIds)
            """.trimIndent()
        )
            .bind("amountValue", amountValue)
            .bind("limitIds", limitIds)
            .await()
    }
}
