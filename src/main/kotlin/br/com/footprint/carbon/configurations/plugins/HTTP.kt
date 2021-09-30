package br.com.footprint.carbon.configurations.plugins

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod

fun Application.configureHTTP() {
    install(CORS) {
        method(HttpMethod.Post)
        method(HttpMethod.Get)
        method(HttpMethod.Options)
        header(HttpHeaders.AccessControlAllowOrigin)
        header(HttpHeaders.ContentType)
        anyHost()
    }
}
