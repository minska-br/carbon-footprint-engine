package br.com.footprint.carbon.application.services.requests

data class RecipeRequest (
    val name: String,
    val ingredients: List<Ingredient>,
    val directions: List<Direction>
)

data class Ingredient (
    val name: String,
    val amount: Double,
)

data class Direction (
    val step: Int,
    val name: String,
    val amount: Double? = null,
)