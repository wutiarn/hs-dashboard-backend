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
    suspend fun sendTransactionNotification(transactionEntity: TransactionEntity) {
        val text = """
            ${transactionEntity.amount} ${transactionEntity.merchant}
            Limit: ${transactionEntity.remainingBalance}
            """.trimIndent()

        telegramMessageService.sendMessage(
            chat = budgetAnalyzerTelegramProperties.targetChat,
            text = text
        )
    }
}
