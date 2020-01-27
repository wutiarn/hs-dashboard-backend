package ru.wtrn.telegram.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import ru.wtrn.telegram.configuration.properties.TelegramProperties

@Configuration
@EnableConfigurationProperties(TelegramProperties::class)
class TelegramConfiguration {

}
