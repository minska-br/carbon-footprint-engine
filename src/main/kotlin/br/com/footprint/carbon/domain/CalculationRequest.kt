package br.com.footprint.carbon.domain

import java.util.UUID

class CalculationRequest(
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
