package br.com.footprint.carbon.infrastructure.repositories.gateways

import br.com.footprint.carbon.application.services.requests.Ingredient
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.math.BigDecimal


data class ProductFootprint(
    val productName: String,
)

data class CarbonEmission (
    val value: BigDecimal,
    val unit: EmissionUnit
)

enum class EmissionUnit {
    KG_CO2_EQ
}

class LifeCycleAssessmentGateway {
    companion object {
        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = GsonSerializer() {
                    setPrettyPrinting()
                    disableHtmlEscaping()
                }
            }
        }
    }

    suspend fun calculateForFoods(foods: List<Ingredient>) : List<ProductFootprint> {
        return client.post("http://localhost:8000/calculate") {
            contentType(ContentType.Application.Json)
            body = foods
        }
    }
}