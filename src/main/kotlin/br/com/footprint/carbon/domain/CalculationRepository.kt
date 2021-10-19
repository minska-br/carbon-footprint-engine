package br.com.footprint.carbon.domain

interface CalculationRepository {
    fun saveOrUpdate(calculation: Calculation)
    fun findById(id: String): Calculation?
}
