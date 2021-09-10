package br.com.footprint.carbon.application.services

import br.com.footprint.carbon.domain.Calculation
import br.com.footprint.carbon.domain.CalculationRepository
import br.com.footprint.carbon.domain.CalculationRequest
import br.com.footprint.carbon.domain.CalculationRequestRepository
import br.com.footprint.carbon.domain.HttpMethod
import br.com.footprint.carbon.domain.ProcessesCalculationRepository
import br.com.footprint.carbon.infrastructure.gateways.LifeCycleAssessmentGateway
import br.com.footprint.carbon.infrastructure.gateways.RecipeGateway
import br.com.footprint.carbon.infrastructure.gateways.responses.Ingredient
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class Product(
    val foodName: String,
    val type: FoodType,
    val amount: BigDecimal,
    val recipeId: String?,
)

enum class FoodType { PRODUCT, RECIPE }

class CalculateCarbonFootprintService(
    private val lifeCycleAssessmentGateway: LifeCycleAssessmentGateway,
    private val recipeGateway: RecipeGateway,
    private val calculationRequestRepository: CalculationRequestRepository,
    private val processesCalculationRepository: ProcessesCalculationRepository,
    private val calculationRepository: CalculationRepository
) {
    private var logger = LoggerFactory.getLogger(CalculateCarbonFootprintService::class.java)

    fun calculate(product: Product): CalculationRequest {
        val ingredients = product.recipeId?.let { recipeId ->
            recipeGateway.getRecipeById(recipeId).ingredients
        } ?: run {
            listOf(
                Ingredient(name = product.foodName, amount = product.amount)
            )
        }

        val calculationRequestId = lifeCycleAssessmentGateway.calculateForFoods(ingredients).also {
            logger.info("Requesting the calculation for the ingredients sent from the product: ${product.foodName}")
        }

        return CalculationRequest(
            requestId = UUID.randomUUID().toString(),
            calculationId = calculationRequestId.value.toString(),
            name = product.foodName,
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
