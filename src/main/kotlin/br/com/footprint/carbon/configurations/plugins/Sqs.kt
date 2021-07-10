package br.com.footprint.carbon.configurations.plugins

import br.com.footprint.carbon.infrastructure.listeners.CalculationCompletedListener
import io.ktor.application.Application
import kotlinx.coroutines.runBlocking
import org.koin.ktor.ext.inject

fun Application.configureSqs() {
    val sqsSampleConsumerChannels by inject<CalculationCompletedListener>()

    runBlocking {
        println("${Thread.currentThread().name} Starting program")
        sqsSampleConsumerChannels.start()
    }
}
