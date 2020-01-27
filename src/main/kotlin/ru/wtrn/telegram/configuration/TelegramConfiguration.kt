package ru.wtrn.telegram.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.wtrn.telegram.configuration.properties.TelegramProperties
import ru.wtrn.telegram.service.TelegramWebhookService

@Configuration
@EnableConfigurationProperties(TelegramProperties::class)
@Import(TelegramWebhookService::class)
class TelegramConfiguration
