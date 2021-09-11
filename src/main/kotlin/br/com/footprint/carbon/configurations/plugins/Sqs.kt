package br.com.footprint.carbon.configurations.plugins

import br.com.footprint.carbon.infrastructure.listeners.CompletedCalculationListener
import br.com.footprint.carbon.infrastructure.listeners.FailedCalculationListener
import io.ktor.application.Application
import kotlinx.coroutines.runBlocking
import org.koin.ktor.ext.inject

fun Application.configureSqs() {
    val completedCalculationListener by inject<CompletedCalculationListener>()
    val failedCalculationListener by inject<FailedCalculationListener>()

    runBlocking {
        completedCalculationListener.start()
        failedCalculationListener.start()
    }
}
