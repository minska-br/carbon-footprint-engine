package br.com.application

import br.com.domain.CalculationRepository
import br.com.domain.CalculationRequest
import br.com.domain.HttpMethod
import br.com.domain.Recipe
import br.com.infrastructure.LifeCycleAssessmentGateway
import java.time.LocalDateTime
import java.util.UUID

class CalculateCarbonFootprintService(
    private val lifeCycleAssessmentGateway: LifeCycleAssessmentGateway,
    private val calculationRepository: CalculationRepository
) {
    suspend fun calculate(recipe: Recipe) : CalculationRequest {
        lifeCycleAssessmentGateway.calculateForFoods(recipe.ingredients)

        return CalculationRequest(
            id = UUID.randomUUID().toString(),
            href = "/calculation/requests/{request_id}",
            method = HttpMethod.GET,
            startTime = LocalDateTime.now().toString()
        ).also {
            calculationRepository.saveCalculationRequest(it)
        }
    }

    fun getAllCalculationRequest() : List<CalculationRequest> =
        calculationRepository.findAll()

    fun getCalculationRequest(calculationRequestId: UUID) : CalculationRequest =
        calculationRepository.findCalculationRequestById(calculationRequestId)

}
