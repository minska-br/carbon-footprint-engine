package br.com.footprint.carbon.application.services

import br.com.footprint.carbon.application.services.requests.RecipeRequest
import br.com.footprint.carbon.domain.CalculationRepository
import br.com.footprint.carbon.domain.CalculationRequest
import br.com.footprint.carbon.domain.HttpMethod
import br.com.footprint.carbon.infrastructure.repositories.gateways.LifeCycleAssessmentGateway
import java.time.LocalDateTime
import java.util.UUID

class CalculateCarbonFootprintService(
    private val lifeCycleAssessmentGateway: LifeCycleAssessmentGateway,
    private val calculationRepository: CalculationRepository
) {
    suspend fun calculate(recipe: RecipeRequest): CalculationRequest {
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
