package br.com.footprint.carbon.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

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
    var totalCarbonFootprint: BigDecimal = processes.sumOf { it.value }
}

enum class UnitType {
    KG_CO2_EQ
}

data class Processes(
    val name: String,
    val value: BigDecimal,
    val unit: String = UnitType.KG_CO2_EQ.toString(),
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
