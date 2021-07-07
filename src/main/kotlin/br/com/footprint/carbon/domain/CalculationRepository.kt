package br.com.footprint.carbon.domain

import java.util.UUID

interface CalculationRepository {
    fun saveCalculationRequest(calculationRequest: CalculationRequest)
    fun findCalculationRequestById(id: UUID) : CalculationRequest
    fun findAll() : List<CalculationRequest>
}