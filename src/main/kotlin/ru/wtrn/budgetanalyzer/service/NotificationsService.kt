package ru.wtrn.budgetanalyzer.service

import org.springframework.stereotype.Service
import ru.wtrn.budgetanalyzer.configuration.properties.BudgetAnalyzerTelegramProperties
import ru.wtrn.budgetanalyzer.entity.TransactionEntity
import ru.wtrn.telegram.service.TelegramMessageService

@Service
class NotificationsService(
    private val telegramMessageService: TelegramMessageService,
    private val budgetAnalyzerTelegramProperties: BudgetAnalyzerTelegramProperties
) {
    suspend fun sendTransactionNotification(transactionEntity: TransactionEntity, remainingLimit: LimitsService.RemainingLimit) {
        val text = """
            ${transactionEntity.amount} ${transactionEntity.merchant}
            Day: ${remainingLimit.day.value}
            Month: ${remainingLimit.month.currency}
            Card balance: ${transactionEntity.remainingBalance}
            """.trimIndent()

        telegramMessageService.sendMessage(
            chat = budgetAnalyzerTelegramProperties.targetChat,
            text = text
        )
    }
}
