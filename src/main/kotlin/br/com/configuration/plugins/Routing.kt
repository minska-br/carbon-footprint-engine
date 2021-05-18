package br.com.configuration.plugins

import br.com.application.CalculateCarbonFootprintService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import org.koin.ktor.ext.inject
import java.util.UUID

fun Application.configureRouting(

) {
    val calculateCarbonFootprintService by inject<CalculateCarbonFootprintService>()

    /*
    - GET /food?name="" 200 {"id": UUID, "name": String, "ingredients": [String], "process": [String]}
    - POST /calculation = 202 Accepted {"request_id": UUID, "href": "/calculation/requests/{request_id}, "method": "GET", "startTime": Datetime }
    - GET /calculation/{calculation_id} = 200 Accepted {"id": UUID, "amount": BigDecimal, "Unit": "", "process": [{"name": String, "description": "String", "Amount": BigDecimal, "Unit": ENUM}]}
    - GET /calculation/requests/{request_id} = 200 Accepted {"calculation_id": UUID?,  "request_id": UUID, "status": String, "startTime": Datetime, "endTime": Datetime}
    - GET /calculation/requests = 200 Accepted [{"calculation_id": UUID?, , "request_id": UUID, "status": String, "startTime": Datetime, "endTime": Datetime}, etc]
    */

    routing {
        get("/food") {
            call.request.queryParameters["name"].takeUnless { it.isNullOrEmpty() }?.let {
                TODO("Not implemented yet")
            }
        }
        route("/calculation") {
            post {
                val calculationRequest = calculateCarbonFootprintService.calculate(call.receive())
                call.respond(HttpStatusCode.Accepted, calculationRequest)
            }

            get("{calculationId}") {
                TODO("Not yet implemented")
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
                        calculationRequestId = UUID.fromString(id)
                    )
                    call.respond(HttpStatusCode.OK, calculationRequest)
                }
            }
        }
    }
}