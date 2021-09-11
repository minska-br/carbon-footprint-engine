package br.com.footprint.carbon.configurations.plugins

import br.com.footprint.carbon.application.services.CalculateCarbonFootprintService
import br.com.footprint.carbon.configurations.mongoDatabase
import br.com.footprint.carbon.domain.CalculationRepository
import br.com.footprint.carbon.domain.CalculationRequestRepository
import br.com.footprint.carbon.domain.ProcessesCalculationRepository
import br.com.footprint.carbon.infrastructure.gateways.LifeCycleAssessmentGateway
import br.com.footprint.carbon.infrastructure.gateways.RecipeGateway
import br.com.footprint.carbon.infrastructure.listeners.CompletedCalculationListener
import br.com.footprint.carbon.infrastructure.listeners.FailedCalculationListener
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
                    "MONGODB_DATABASE" to getKey("ktor.application.mongodb.database"),
                    "LCA_API_URL" to getKey("ktor.application.api.lcaUrl"),
                    "RECIPE_URL" to getKey("ktor.application.api.recipeUrl"),
                    "SQS_URI" to getKey("ktor.application.sqs.uri"),
                    "SQS_URL" to getKey("ktor.application.sqs.url"),
                    "DLQ_SQS_URL" to getKey("ktor.application.sqs.dlqUrl")
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
                databaseName = keys.getValue("MONGODB_DATABASE")
            )
        }

        single { CalculateCarbonFootprintService(get(), get(), get(), get(), get()) }
        single { LifeCycleAssessmentGateway(keys.getValue("LCA_API_URL")) }
        single { RecipeGateway(keys.getValue("RECIPE_URL")) }
        single<CalculationRequestRepository> { CalculationRequestRepositoryImpl(get()) }
        single<ProcessesCalculationRepository> { ProcessesCalculationRepositoryImpl(get()) }
        single<CalculationRepository> { CalculationRepositoryImpl(get()) }
        single {
            CompletedCalculationListener(keys.getValue("SQS_URI"), keys.getValue("SQS_URL"), get(), get())
        }
        single {
            FailedCalculationListener(keys.getValue("SQS_URI"), keys.getValue("DLQ_SQS_URL"), get())
        }
    }

data class KeyNotFound(val msg: String) : RuntimeException(msg)

fun Application.getKey(key: String): String {
    return environment.config.propertyOrNull(key)?.getString()
        ?: throw KeyNotFound("Key '$key' not found")
}
