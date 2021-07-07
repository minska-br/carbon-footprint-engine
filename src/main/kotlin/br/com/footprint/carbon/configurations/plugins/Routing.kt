package br.com.footprint.carbon.configurations.plugins

import br.com.footprint.carbon.application.services.CalculateCarbonFootprintService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val calculateCarbonFootprintService by inject<CalculateCarbonFootprintService>()

    routing {
        route("/calculation") {
            post {
                val calculationRequest = calculateCarbonFootprintService.calculate(call.receive())
                call.respond(HttpStatusCode.Accepted, calculationRequest)
            }
        }
    }
}
