package br.com.footprint.carbon.infrastructure.gateways

import br.com.footprint.carbon.infrastructure.gateways.responses.Ingredient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import java.util.UUID

data class RequestId(val value: UUID)

data class CalculationBody(
    val id: UUID?,
    val products: List<Ingredient>
)

class LifeCycleAssessmentGateway(val lcaApiUrl: String) {
    companion object {
        val client = HttpClient(CIO) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }
            install(JsonFeature) {
                serializer = GsonSerializer() {
                    setPrettyPrinting()
                    disableHtmlEscaping()
                }
            }
        }
    }

    fun calculateForFoods(foods: List<Ingredient>, calculationId: UUID? = null) = runBlocking {
        client.post<RequestId>("$lcaApiUrl/calculate") {
            contentType(ContentType.Application.Json)
            body = CalculationBody(id = calculationId, products = foods)
        }
    }
}
