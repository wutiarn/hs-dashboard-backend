package ru.wtrn.budgetanalyzer.repository

import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import ru.wtrn.budgetanalyzer.entity.ManualLimitUpdateEntity
import ru.wtrn.budgetanalyzer.entity.TransactionEntity
import ru.wtrn.budgetanalyzer.support.CoroutineCrudRepository
import java.util.UUID

@Repository
class ManualLimitUpdateRepository(
    databaseClient: DatabaseClient
) : CoroutineCrudRepository<ManualLimitUpdateEntity, UUID>(
    domainType = ManualLimitUpdateEntity::class.java,
    idColumn = "id",
    databaseClient = databaseClient
)
