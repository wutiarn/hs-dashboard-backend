package ru.wtrn.budgetanalyzer.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import ru.wtrn.budgetanalyzer.model.Amount
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

@Table("transactions")
data class TransactionEntity(
    val bank: Bank,
    val cardPanSuffix: String,
    val timestamp: LocalDateTime,
    val merchant: String,
    val location: String,
    val amount: Amount,
    val remainingBalance: Amount,

    @Id
    val id: UUID = UUID.randomUUID(),
    val createdAt: Instant? = Instant.now()
) {
    enum class Bank {
        MTS_BANK
    }
}
