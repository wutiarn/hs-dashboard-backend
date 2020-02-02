package ru.wtrn.budgetanalyzer.service

import org.springframework.stereotype.Service
import ru.wtrn.budgetanalyzer.configuration.properties.BudgetAnalyzerTelegramProperties
import ru.wtrn.budgetanalyzer.entity.TransactionEntity
import ru.wtrn.budgetanalyzer.model.Amount
import ru.wtrn.budgetanalyzer.util.limitAmount
import ru.wtrn.budgetanalyzer.util.remainingAmount
import ru.wtrn.telegram.service.TelegramMessageService

@Service
class NotificationsService(
    private val telegramMessageService: TelegramMessageService,
    private val budgetAnalyzerTelegramProperties: BudgetAnalyzerTelegramProperties
) {
    suspend fun sendTransactionNotification(transactionEntity: TransactionEntity, resultingLimits: LimitsService.ResultingLimits) {
        val text = """
            ${transactionEntity.amount} ${transactionEntity.merchant}
            Осталось: ${resultingLimits.todayLimit.remainingAmount} 
            Завтра: ${resultingLimits.nextDayCalculatedLimit.limitAmount}
            До конца месяца: ${resultingLimits.monthLimit.remainingAmount} 
            Баланс карты: ${transactionEntity.remainingBalance}
            """.trimIndent()

        telegramMessageService.sendMessage(
            chat = budgetAnalyzerTelegramProperties.targetChat,
            text = text
        )
    }

    suspend fun sendManualLimitUpdateNotification(
        amount: Amount,
        description: String?,
        resultingLimits: LimitsService.ResultingLimits
    ) {
        val text = """
            $amount ${description ?: ""}
            Осталось: ${resultingLimits.todayLimit.remainingAmount} 
            Завтра: ${resultingLimits.nextDayCalculatedLimit.limitAmount}
            До конца месяца: ${resultingLimits.monthLimit.remainingAmount}
            """.trimIndent()

        telegramMessageService.sendMessage(
            chat = budgetAnalyzerTelegramProperties.targetChat,
            text = text
        )
    }
}
