package ru.wtrn.budgetanalyzer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class BudgetAnalyzerApplication

fun main(args: Array<String>) {
	runApplication<BudgetAnalyzerApplication>(*args)
}
