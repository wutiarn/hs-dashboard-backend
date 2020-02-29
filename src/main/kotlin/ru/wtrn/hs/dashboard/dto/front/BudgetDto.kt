package ru.wtrn.hs.dashboard.dto.front

data class BudgetDto(
    val balance: String,
    val today: String,
    val tomorrow: String,
    val month: String,
    val cardBalance: String
)
