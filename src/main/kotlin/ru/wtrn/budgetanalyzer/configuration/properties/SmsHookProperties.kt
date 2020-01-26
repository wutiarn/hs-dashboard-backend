package ru.wtrn.budgetanalyzer.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.util.UUID

@ConstructorBinding
@ConfigurationProperties("budget-analyzer.sms-hook")
data class SmsHookProperties(
    val secret: UUID
)
