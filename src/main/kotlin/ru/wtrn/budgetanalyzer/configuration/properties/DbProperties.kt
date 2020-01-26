package ru.wtrn.budgetanalyzer.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("budget-analyzer.db")
data class DbProperties(
    val host: String,
    val port: Int = 5432,
    val database: String,
    val username: String,
    val password: String,

    val liquibaseChangelog: String
)
