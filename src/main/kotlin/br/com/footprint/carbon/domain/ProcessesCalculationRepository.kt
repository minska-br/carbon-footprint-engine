package br.com.footprint.carbon.domain

interface ProcessCalculationsRepository {
    fun saveCalculationCompletedEvent(event: CalculationCompletedEvent)
    fun findProcessCalculationByCalculationId(id: String): CalculationCompletedEvent
}