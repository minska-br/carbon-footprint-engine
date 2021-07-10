package br.com.footprint.carbon.domain

interface ProcessesCalculationRepository {
    fun save(processesCalculation: ProcessesCalculation)
    fun findByCalculationId(id: String): ProcessesCalculation
}
