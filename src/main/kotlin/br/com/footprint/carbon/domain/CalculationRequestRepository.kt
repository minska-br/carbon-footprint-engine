package br.com.footprint.carbon.domain

interface CalculationRequestRepository {
    fun saveOrUpdateCalculationRequest(calculationRequest: CalculationRequest)
    fun findAll(): List<CalculationRequest>
    fun findById(id: String): CalculationRequest
    fun findByCalculationId(id: String): CalculationRequest
    fun updateStatusByCalculationId(calculationId: String, status: CalculationRequestStatus, endTime: String)
}
