package ru.wtrn.hs.dashboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class HsDashboardBackendApplication

fun main(args: Array<String>) {
	runApplication<HsDashboardBackendApplication>(*args)
}
