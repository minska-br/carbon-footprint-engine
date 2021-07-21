package br.com.footprint.carbon.configurations.plugins

import br.com.footprint.carbon.application.services.CalculateCarbonFootprintService
import br.com.footprint.carbon.configurations.mongoDatabase
import br.com.footprint.carbon.domain.CalculationRepository
import br.com.footprint.carbon.domain.CalculationRequestRepository
import br.com.footprint.carbon.domain.ProcessesCalculationRepository
import br.com.footprint.carbon.infrastructure.gateways.LifeCycleAssessmentGateway
import br.com.footprint.carbon.infrastructure.listeners.CalculationCompletedListener
import br.com.footprint.carbon.infrastructure.repositories.CalculationRepositoryImpl
import br.com.footprint.carbon.infrastructure.repositories.CalculationRequestRepositoryImpl
import br.com.footprint.carbon.infrastructure.repositories.ProcessesCalculationRepositoryImpl
import com.mongodb.ConnectionString
import io.ktor.application.Application
import io.ktor.application.install
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(
            modules(
                mapOf(
                    "MONGODB_CONNECTION_URL" to getKey("ktor.application.mongodb.connectionUrl"),
                    "MONGODB_USER" to getKey("ktor.application.mongodb.user"),
                    "MONGODB_PASSWORD" to getKey("ktor.application.mongodb.password"),
                    "MONGODB_DATABASE" to getKey("ktor.application.mongodb.database"),
                    "LCA_API_URL" to getKey("ktor.application.api.lcaUrl"),
                    "SQS_URI" to getKey("ktor.application.sqs.uri"),
                    "SQS_URL" to getKey("ktor.application.sqs.url"),
                )
            )
        )
    }
}

fun modules(keys: Map<String, String>): Module =
    module {
        single {
            mongoDatabase(
                connectionString = ConnectionString(keys.getValue("MONGODB_CONNECTION_URL")),
                username = keys.getValue("MONGODB_USER"),
                password = keys.getValue("MONGODB_PASSWORD"),
                databaseName = keys.getValue("MONGODB_DATABASE")
            )
        }

        single { CalculateCarbonFootprintService(get(), get(), get(), get()) }
        single { LifeCycleAssessmentGateway(keys.getValue("LCA_API_URL")) }
        single<CalculationRequestRepository> { CalculationRequestRepositoryImpl(get()) }
        single<ProcessesCalculationRepository> { ProcessesCalculationRepositoryImpl(get()) }
        single<CalculationRepository> { CalculationRepositoryImpl(get()) }
        single {
            CalculationCompletedListener(keys.getValue("SQS_URI"), keys.getValue("SQS_URL"), get(), get())
        }
    }

data class KeyNotFound(val msg: String) : RuntimeException(msg)

fun Application.getKey(key: String): String {
    return environment.config.propertyOrNull(key)?.getString()
        ?: throw KeyNotFound("Key '$key' not found")
}
