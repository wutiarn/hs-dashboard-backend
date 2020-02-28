package ru.wtrn.hs.dashboard.support

import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.await
import org.springframework.data.r2dbc.core.awaitFirstOrNull
import org.springframework.data.r2dbc.query.Criteria

abstract class CoroutineCrudRepository<T : Any, ID : Any>(
    protected val domainType: Class<T>,
    protected val idColumn: String,
    protected val databaseClient: DatabaseClient
) {
    open suspend fun findById(id: ID): T? =
        databaseClient.select()
            .from(domainType)
            .matching(
                Criteria.where(idColumn).`is`(id)
            )
            .fetch()
            .awaitFirstOrNull()

    open suspend fun insert(entity: T) {
        databaseClient.insert()
            .into(domainType)
            .using(entity)
            .await()
    }
}
