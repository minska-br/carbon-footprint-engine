package br.com.footprint.carbon.domain

interface CalculationRepository {
    fun save(calculation: Calculation)
    fun findById(id: String): Calculation?
}
