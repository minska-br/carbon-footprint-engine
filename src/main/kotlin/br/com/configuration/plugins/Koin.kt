package br.com.configuration.plugins

import br.com.application.CalculateCarbonFootprintService
import br.com.domain.CalculationRepository
import br.com.infrastructure.LifeCycleAssessmentGateway
import br.com.infrastructure.repositories.CalculationRepositoryImpl
import com.mongodb.ConnectionString
import io.ktor.application.Application
import io.ktor.application.install
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.logger.slf4jLogger

val modules = module {
    single {
        mongoDatabase(
            connectionString = ConnectionString("mongodb://localhost:27017"),
            username = "root",
            password = "root",
            databaseName = "CarbonFootprint"
        )
    }
    single { CalculateCarbonFootprintService(get(), get()) }
    single { LifeCycleAssessmentGateway() }
    single<CalculationRepository> { CalculationRepositoryImpl(get()) }
}

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(modules)
    }
}