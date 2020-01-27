package ru.wtrn.telegram.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("wtrn.telegram")
@ConstructorBinding
data class TelegramProperties(
    val webhookKey: String,
    val botKey: String,
    val httpProxy: String? = null
)
