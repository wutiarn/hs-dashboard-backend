package ru.wtrn.budgetanalyzer.util

import ru.wtrn.budgetanalyzer.entity.CurrentLimitEntity
import ru.wtrn.budgetanalyzer.model.Amount

val CurrentLimitEntity.spentAmount: Amount
    get() = Amount(
        value = spentValue,
        currency = currency
    )

val CurrentLimitEntity.remainingAmount: Amount
    get() = Amount(
        value = limitValue - (spentValue),
        currency = currency
    )

val CurrentLimitEntity.limitAmount: Amount
    get() = Amount(
        value = limitValue,
        currency = currency
    )

val CurrentLimitEntity.budgetBalance: Amount
    get() = Amount(
        value = limitValue,
        currency = currency
    )
