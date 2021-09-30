package br.com.footprint.carbon.infrastructure.gateways.responses

import java.math.BigDecimal

data class RecipeResponse(
    val name: String,
    val ingredients: List<Ingredient>,
    val directions: List<Direction>
)

data class Ingredient(
    val name: String,
    val amount: BigDecimal,
    val unit: String,
)

data class Direction(
    val step: Int,
    val name: String,
    val amount: Double? = null,
)
