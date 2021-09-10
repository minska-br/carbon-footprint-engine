package br.com.footprint.carbon.infrastructure.gateways

import br.com.footprint.carbon.infrastructure.gateways.responses.RecipeResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.timeout
import io.ktor.client.request.post
import kotlinx.coroutines.runBlocking

const val REQUEST_TIMEOUT_MILLIS = 200000L

class RecipeGateway(private val recipeUrl: String) {
    companion object {
        val client = HttpClient(CIO) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }
            install(HttpTimeout)
            install(JsonFeature) {
                serializer = GsonSerializer() {
                    setPrettyPrinting()
                    disableHtmlEscaping()
                }
            }
        }
    }

    fun getRecipeById(id: String) = runBlocking {
        client.post<RecipeResponse>("$recipeUrl/recipes/AllRecipes/$id") {
            timeout {
                requestTimeoutMillis = REQUEST_TIMEOUT_MILLIS
            }
        }
    }
}
