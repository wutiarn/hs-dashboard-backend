package ru.wtrn.budgetanalyzer.entity

import org.springframework.data.relational.core.mapping.Table
import ru.wtrn.budgetanalyzer.model.Amount
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Table("current_limits")
data class CurrentLimitEntity(
    val spentAmount: Amount,
    val limitAmount: Amount,

    val tag: String,
    val timespan: LimitTimespan,
    val validUntil: Instant,
    val periodStart: LocalDate,

    val createdAt: Instant = Instant.now(),
    val id: UUID = UUID.randomUUID()
) {
    enum class LimitTimespan {
        DAY,
        MONTH
    }
}
