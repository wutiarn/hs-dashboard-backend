package ru.wtrn.budgetanalyzer.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ru.wtrn.budgetanalyzer.configuration.properties.BudgetAnalyzerTelegramProperties
import ru.wtrn.budgetanalyzer.configuration.properties.SmsHookProperties
import ru.wtrn.budgetanalyzer.entity.TransactionEntity
import ru.wtrn.budgetanalyzer.exception.UnknownSmsHookSecretException
import ru.wtrn.budgetanalyzer.model.SmsMessage
import ru.wtrn.budgetanalyzer.parser.MtsBankSmsParser
import ru.wtrn.budgetanalyzer.repository.TransactionRepository
import ru.wtrn.telegram.service.TelegramMessageService

@Service
class SmsMessageRoutingService(
    private val mtsBankSmsParser: MtsBankSmsParser,
    private val smsHookProperties: SmsHookProperties,
    private val transactionRepository: TransactionRepository,
    private val notificationsService: NotificationsService,
    private val limitsService: LimitsService
) {
    private val logger = KotlinLogging.logger {  }

    suspend fun handleMessage(message: SmsMessage) {
        if (message.secret != smsHookProperties.secret) {
            throw UnknownSmsHookSecretException()
        }

        val transactionEntity = when (message.from) {
            "MTS-Bank" -> mtsBankSmsParser.parseForTransaction(
                message = message.body,
                receivedAt = message.timestamp
            )
            else -> return
        }

        logger.info { "Creating ${transactionEntity.id} transaction" }

        transactionRepository.insert(transactionEntity)

        val remainingLimit = limitsService.decreaseLimit(transactionEntity.amount)

        notificationsService.sendTransactionNotification(transactionEntity, remainingLimit)
    }
}
