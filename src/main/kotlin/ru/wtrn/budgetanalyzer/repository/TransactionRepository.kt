package ru.wtrn.budgetanalyzer.repository

import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import ru.wtrn.budgetanalyzer.entity.TransactionEntity
import ru.wtrn.budgetanalyzer.support.CoroutineCrudRepository
import java.util.UUID


@Repository
class TransactionRepository(
    databaseClient: DatabaseClient
) : CoroutineCrudRepository<TransactionEntity, UUID>(
    domainType = TransactionEntity::class.java,
    idColumn = "id",
    databaseClient = databaseClient
)
