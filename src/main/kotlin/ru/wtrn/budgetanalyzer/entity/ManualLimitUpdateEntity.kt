package ru.wtrn.budgetanalyzer.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import ru.wtrn.budgetanalyzer.model.Amount
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

@Table("manual_limit_updates")
data class ManualLimitUpdateEntity(
    val amountValue: BigDecimal,
    val description: String?,
    val author: String,
    val timestamp: Instant = Instant.now(),

    @Id
    val id: UUID = UUID.randomUUID()
)
