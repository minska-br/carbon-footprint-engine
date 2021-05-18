package br.com.infrastructure

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RecipeGateway {
    companion object {
        val client = HttpClient(Apache)
        const val baseUrl = "https://api.spoonacular.com"
        const val apiKey = "a6c65bff02ae4c3ea7a295e5cf0ff6dc"
        const val url = "/recipes/complexSearch"
    }

    suspend fun findRecipes(foodName: String) : List<ProductFootprint> {
        return client.get(baseUrl + url) {
            parameter("apiKey", apiKey)
            parameter("query", foodName)
        }
    }
}