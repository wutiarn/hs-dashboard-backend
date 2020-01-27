package ru.wtrn.budgetanalyzer.service

import org.springframework.stereotype.Service
import ru.wtrn.budgetanalyzer.configuration.properties.BudgetAnalyzerTelegramProperties
import ru.wtrn.budgetanalyzer.configuration.properties.SmsHookProperties
import ru.wtrn.budgetanalyzer.entity.OperationEntity
import ru.wtrn.budgetanalyzer.exception.UnknownSmsHookSecretException
import ru.wtrn.budgetanalyzer.model.SmsMessage
import ru.wtrn.budgetanalyzer.parser.MtsBankSmsParser
import ru.wtrn.budgetanalyzer.repository.OperationRepository
import ru.wtrn.telegram.service.TelegramMessageService

@Service
class SmsMessageRoutingService(
    private val mtsBankSmsParser: MtsBankSmsParser,
    private val smsHookProperties: SmsHookProperties,
    private val operationRepository: OperationRepository,
    private val telegramMessageService: TelegramMessageService,
    private val budgetAnalyzerTelegramProperties: BudgetAnalyzerTelegramProperties
) {
    suspend fun handleMessage(message: SmsMessage) {
        if (message.secret != smsHookProperties.secret) {
            throw UnknownSmsHookSecretException()
        }

        val parsedMessage = mtsBankSmsParser.parseMessage(
            message = message.body,
            receivedAt = message.timestamp
        )

        val operationEntity = OperationEntity(
            cardPanSuffix = parsedMessage.panSuffix,
            timestamp = parsedMessage.timestamp,
            merchant = parsedMessage.merchant,
            location = parsedMessage.location,
            amount = parsedMessage.amount,
            remainingBalance = parsedMessage.remainingBalance
        )

        operationRepository.insert(operationEntity)

        telegramMessageService.sendMessage(
            chat = budgetAnalyzerTelegramProperties.targetChat,
            text = "${operationEntity.amount.value} ${operationEntity.amount.currency.currencyCode} ${operationEntity.merchant}"
        )
    }
}
