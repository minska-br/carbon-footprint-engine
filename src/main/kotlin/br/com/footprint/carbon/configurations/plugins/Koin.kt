package br.com.footprint.carbon.configurations.plugins

import br.com.footprint.carbon.configurations.mongoDatabase
import br.com.footprint.carbon.domain.CalculationRepository
import br.com.footprint.carbon.infrastructure.repositories.CalculationRepositoryImpl
import com.mongodb.ConnectionString
import io.ktor.application.Application
import io.ktor.application.install
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(modules)
    }
}

private val modules = module {
    single {
        mongoDatabase(
            connectionString = ConnectionString("mongodb://localhost:27017"),
            username = "root",
            password = "root",
            databaseName = "CarbonFootprint"
        )
    }
    single<CalculationRepository> { CalculationRepositoryImpl(get()) }
}
