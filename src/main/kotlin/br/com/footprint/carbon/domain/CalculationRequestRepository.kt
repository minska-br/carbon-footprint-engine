package br.com.footprint.carbon.domain

interface CalculationRequestRepository {
    fun saveCalculationRequest(calculationRequest: CalculationRequest)
    fun findAll(): List<CalculationRequest>
    fun findById(id: String): CalculationRequest
    fun findByCalculationId(id: String): CalculationRequest
    fun updateStatusByCalculationId(calculationId: String, status: CalculationRequestStatus)
}
