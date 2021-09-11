package br.com.footprint.carbon.domain

data class CalculationRequest(
    val requestId: String,
    val calculationId: String,
    val name: String,
    val href: String,
    val method: HttpMethod,
    val startTime: String,
    val endTime: String? = null,
    val status: CalculationRequestStatus = CalculationRequestStatus.CALCULATING
)

enum class HttpMethod {
    GET
}

enum class CalculationRequestStatus {
    CALCULATING,
    CALCULATED,
    ERROR
}
