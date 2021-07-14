package br.com.footprint.carbon.application.services

import br.com.footprint.carbon.application.services.requests.RecipeRequest
import br.com.footprint.carbon.domain.Calculation
import br.com.footprint.carbon.domain.CalculationRepository
import br.com.footprint.carbon.domain.CalculationRequest
import br.com.footprint.carbon.domain.CalculationRequestRepository
import br.com.footprint.carbon.domain.HttpMethod
import br.com.footprint.carbon.domain.ProcessesCalculationRepository
import br.com.footprint.carbon.infrastructure.gateways.LifeCycleAssessmentGateway
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.util.UUID

class CalculateCarbonFootprintService(
    private val lifeCycleAssessmentGateway: LifeCycleAssessmentGateway,
    private val calculationRequestRepository: CalculationRequestRepository,
    private val processesCalculationRepository: ProcessesCalculationRepository,
    private val calculationRepository: CalculationRepository
) {
    private var logger = LoggerFactory.getLogger(CalculateCarbonFootprintService::class.java)

    fun calculate(recipe: RecipeRequest): CalculationRequest {
        val calculationRequestId = lifeCycleAssessmentGateway.calculateForFoods(recipe.ingredients).also {
            logger.info("Requesting the calculation for the ingredients sent from the recipe: ${recipe.name}")
        }

        return CalculationRequest(
            requestId = UUID.randomUUID().toString(),
            calculationId = calculationRequestId.value.toString(),
            name = recipe.name,
            href = "/calculation/requests/{request_id}",
            method = HttpMethod.GET,
            startTime = LocalDateTime.now().toString()
        ).also {
            calculationRequestRepository.saveCalculationRequest(it)
        }
    }

    fun getCalculation(id: String): Calculation {
        return calculationRepository.findById(id) ?: run {
            val calculationRequest = calculationRequestRepository.findByCalculationId(id)
            val calculationProcesses = processesCalculationRepository.findByCalculationId(id)

            Calculation(
                id = id,
                name = calculationRequest.name,
                processes = calculationProcesses.processCalculations,
                calculatedPercentage = calculationProcesses.calculatedPercentage
            ).also {
                calculationRepository.save(it)
            }
        }
    }

    fun getAllCalculationRequest(): List<CalculationRequest> =
        calculationRequestRepository.findAll()

    fun getCalculationRequest(calculationRequestId: String): CalculationRequest =
        calculationRequestRepository.findById(calculationRequestId)
}
