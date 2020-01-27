package ru.wtrn.budgetanalyzer.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("budget-analyzer.telegram")
data class BudgetAnalyzerTelegramProperties(
    val targetChat: Int
)
