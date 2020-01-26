package ru.wtrn.budgetanalyzer.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.DATABASE
import io.r2dbc.spi.ConnectionFactoryOptions.DRIVER
import io.r2dbc.spi.ConnectionFactoryOptions.HOST
import io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD
import io.r2dbc.spi.ConnectionFactoryOptions.PORT
import io.r2dbc.spi.ConnectionFactoryOptions.USER
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import ru.wtrn.budgetanalyzer.configuration.properties.DbProperties
import ru.wtrn.budgetanalyzer.support.converter.JsonbReadingConverter
import ru.wtrn.budgetanalyzer.support.converter.JsonbWritingConverter
import ru.wtrn.budgetanalyzer.support.converter.UuidConverterOverride

@Configuration
class R2dbcConfiguration(
    private val dbProperties: DbProperties,
    private val objectMapper: ObjectMapper
) : AbstractR2dbcConfiguration() {
    private val connectionFactory = ConnectionFactories.get(
        ConnectionFactoryOptions.builder()
            .option(DRIVER, "postgresql")
            .option(HOST, dbProperties.host)
            .option(PORT, dbProperties.port)
            .option(USER, dbProperties.username)
            .option(PASSWORD, dbProperties.password)
            .option(DATABASE, dbProperties.database)
            .build()
    )

    override fun connectionFactory(): ConnectionFactory {
        return connectionFactory
    }

    override fun r2dbcCustomConversions(): R2dbcCustomConversions {
        return R2dbcCustomConversions(
            listOf(
                jsonbWritingConverter(),
                JsonbReadingConverter(objectMapper),
                UuidConverterOverride()
            )
        )
    }

    @Bean
    fun transactionManager(): R2dbcTransactionManager {
        return R2dbcTransactionManager(connectionFactory)
    }

    @Bean
    fun jsonbWritingConverter() = JsonbWritingConverter(objectMapper)

    @Bean
    fun liquibase(): DataSourceClosingSpringLiquibase {
        val liquibaseDatasource = DataSourceBuilder.create()
            .url("jdbc:postgresql://${dbProperties.host}:${dbProperties.port}/${dbProperties.database}")
            .username(dbProperties.username)
            .password(dbProperties.password)
            .type(PGSimpleDataSource::class.java)
            .build()

        return DataSourceClosingSpringLiquibase().apply {
            dataSource = liquibaseDatasource
            changeLog = dbProperties.liquibaseChangelog
        }
    }
}
