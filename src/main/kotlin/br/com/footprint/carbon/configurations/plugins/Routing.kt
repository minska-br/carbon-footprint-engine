package br.com.footprint.carbon.configurations.plugins

import br.com.footprint.carbon.application.services.CalculateCarbonFootprintService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.patch
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import org.koin.ktor.ext.inject
import java.util.UUID

fun Application.configureRouting() {
    val calculateCarbonFootprintService by inject<CalculateCarbonFootprintService>()

    routing {
        route("/calculation") {
            post {
                val calculationRequest = calculateCarbonFootprintService.calculate(call.receive())
                call.respond(HttpStatusCode.Accepted, calculationRequest)
            }

            get("/{id}") {
                call.parameters["id"].takeUnless { it.isNullOrEmpty() }?.let { id ->
                    val calculation = calculateCarbonFootprintService.getCalculation(
                        id = UUID.fromString(id).toString()
                    )
                    call.respond(HttpStatusCode.OK, calculation)
                }
            }

            patch("/{id}") {
                call.parameters["id"].takeUnless { it.isNullOrEmpty() }?.let { id ->
                    val calculationRequest = calculateCarbonFootprintService.editCalculation(
                        id = UUID.fromString(id).toString(),
                        newProcesses = call.receive()
                    )
                    call.respond(HttpStatusCode.Accepted, calculationRequest)
                }
            }

            get("/requests") {
                call.respond(
                    HttpStatusCode.OK,
                    calculateCarbonFootprintService.getAllCalculationRequest()
                )
            }

            get("/requests/{id}") {
                call.parameters["id"].takeUnless { it.isNullOrEmpty() }?.let { id ->
                    val calculationRequest = calculateCarbonFootprintService.getCalculationRequest(
                        calculationRequestId = UUID.fromString(id).toString()
                    )
                    call.respond(HttpStatusCode.OK, calculationRequest)
                }
            }
        }
    }
}
