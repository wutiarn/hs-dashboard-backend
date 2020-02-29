package ru.wtrn.hs.dashboard.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.converter.JsonMessageConverter

@Configuration
class KafkaConfiguration {
    @Bean
    fun kafkaListenerContainerFactory(
        configurer: ConcurrentKafkaListenerContainerFactoryConfigurer,
        kafkaProperties: KafkaProperties,
        consumerFactory: ConsumerFactory<Any, Any>,
        objectMapper: ObjectMapper,
        kafkaTemplate: KafkaTemplate<Any, Any>
    ): ConcurrentKafkaListenerContainerFactory<Any, Any> =
        ConcurrentKafkaListenerContainerFactory<Any, Any>().also { factory ->
            configurer.configure(factory, consumerFactory)

            /**
             * Автоматически создает топики, на которые подписывается listener
             */
            factory.containerProperties.isMissingTopicsFatal = false

            /**
             * Обеспечивает десериализацию DTO для аннотированных @Payload полей в @KafkaListener'ах
             */
            factory.setMessageConverter(JsonMessageConverter(objectMapper))
        }
}
