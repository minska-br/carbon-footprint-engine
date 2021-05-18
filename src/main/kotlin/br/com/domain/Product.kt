package br.com.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class Product(
    var name: String,
    var weight: Int
)

// ------------

data class Recipe (
    val id: UUID,
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

// ------------

data class CalculationRequest(
    val id: String,
    val href: String,
    val method: HttpMethod,
    val startTime: String,
    val status: CalculationRequestStatus = CalculationRequestStatus.CALCULATING,
    val calculationId: UUID? = null,
    val endTime: String? = null
)

enum class HttpMethod {
    GET
}

enum class CalculationRequestStatus {
    CALCULATING,
    CALCULATED,
    ERROR
}