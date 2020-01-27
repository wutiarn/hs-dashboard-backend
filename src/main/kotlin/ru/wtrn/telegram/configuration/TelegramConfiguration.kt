package ru.wtrn.telegram.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.tcp.ProxyProvider
import ru.wtrn.telegram.configuration.properties.TelegramProperties
import ru.wtrn.telegram.service.TelegramMessageService
import ru.wtrn.telegram.service.TelegramWebhookService

@Configuration
@EnableConfigurationProperties(TelegramProperties::class)
@Import(TelegramWebhookService::class)
class TelegramConfiguration(
    private val telegramProperties: TelegramProperties
) {
    val webClient = let {
        var httpClient = HttpClient.create()
        telegramProperties.proxy?.let { proxy ->
            httpClient = httpClient.tcpConfiguration {
                it.proxy {
                    it.type(ProxyProvider.Proxy.HTTP)
                        .host(proxy.host)
                        .port(proxy.port)
                }
            }
        }
        WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .baseUrl("https://api.telegram.org/bot${telegramProperties.botKey}/")
            .build()
    }

    @Bean
    fun telegramMessageService(): TelegramMessageService = TelegramMessageService(
        webClient = webClient
    )
}
