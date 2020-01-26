package ru.wtrn.budgetanalyzer.repository

import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import ru.wtrn.budgetanalyzer.entity.OperationEntity
import ru.wtrn.budgetanalyzer.support.CoroutineCrudRepository
import java.util.UUID


@Repository
class OperationRepository(
    databaseClient: DatabaseClient
) : CoroutineCrudRepository<OperationEntity, UUID>(
    domainType = OperationEntity::class.java,
    idColumn = "id",
    databaseClient = databaseClient
)
