package ru.wtrn.telegram.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("wtrn.telegram")
@ConstructorBinding
data class TelegramProperties(
    val webhookKey: String,
    val botKey: String,
    val proxy: ProxyProperties
) {
    data class ProxyProperties(
        val host: String,
        val port: Int
    )
}
