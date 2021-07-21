package br.com.footprint.carbon.infrastructure.gateways

import br.com.footprint.carbon.application.services.requests.Ingredient
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
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

class LifeCycleAssessmentGateway(val LCA_API_URL: String) {
    companion object {
        val client = HttpClient(Apache) {
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

    fun calculateForFoods(foods: List<Ingredient>) = runBlocking {
        client.post<RequestId>("${LCA_API_URL}/calculate") {
            contentType(ContentType.Application.Json)
            body = foods
        }
    }
}
