package br.com.footprint.carbon.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.math.RoundingMode

data class ProcessesCalculation(
    @JsonProperty("calculation_id")
    val calculationId: String,
    @JsonProperty("calculated_percentage")
    val calculatedPercentage: BigDecimal,
    @JsonProperty("process_calculations")
    val processCalculations: List<Processes>
)

data class Calculation(
    val id: String,
    val name: String,
    val unit: UnitType = UnitType.KG_CO2_EQ,
    val processes: List<Processes>,
    val calculatedPercentage: BigDecimal
) {
    companion object {
        private val MEDIUM_CAR = BigDecimal(4.3936731)
        private val MEDIUM_BUS = BigDecimal(0.9768487)
        private val ATLANTIC_FOREST_TREE = BigDecimal(130)
        private val AMAZON_RAINFOREST_TREE = BigDecimal(222)

        private const val ROUND_SCALE = 3
    }

    var totalCarbonFootprint: BigDecimal = processes.sumOf { it.value ?: BigDecimal.ZERO }
    var conversions: List<Conversion> = generateConversions(totalCarbonFootprint)

    private fun generateConversions(totalCarbonFootprint: BigDecimal): List<Conversion> =
        listOf(
            Conversion(
                name = "Medium car",
                value = (totalCarbonFootprint * MEDIUM_CAR).setScale(ROUND_SCALE, RoundingMode.HALF_UP),
                unit = "Quilômetros"
            ),
            Conversion(
                name = "Medium bus",
                value = (totalCarbonFootprint * MEDIUM_BUS).setScale(ROUND_SCALE, RoundingMode.HALF_UP),
                unit = "Quilômetros"
            ),
            Conversion(
                name = "Atlantic Forest tree",
                value = (totalCarbonFootprint / ATLANTIC_FOREST_TREE).setScale(ROUND_SCALE, RoundingMode.HALF_UP),
                unit = "Árvores"
            ),
            Conversion(
                name = "Amazon Rainforest tree",
                value = (totalCarbonFootprint / AMAZON_RAINFOREST_TREE).setScale(ROUND_SCALE, RoundingMode.HALF_UP),
                unit = "Árvores"
            )
        )
}

class Conversion(
    val name: String,
    val value: BigDecimal,
    val unit: String
)

enum class UnitType {
    KG_CO2_EQ
}

data class Processes(
    val name: String,
    val value: BigDecimal?,
    val unit: String = UnitType.KG_CO2_EQ.toString(),
    val amount: BigDecimal,
    @JsonProperty("process_name_found")
    val processNameFound: String?,
    val calculated: Boolean
)

data class Miscalculation(
    @JsonProperty("calculation_id")
    val calculationId: String,
    @JsonProperty("error_message")
    val errorMessage: String,
)
