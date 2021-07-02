package br.com.footprint.carbon

import br.com.footprint.carbon.plugins.configureHTTP
import br.com.footprint.carbon.plugins.configureRouting
import br.com.footprint.carbon.plugins.configureSerialization
import io.ktor.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>): Unit =
    EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    configureRouting()
    configureHTTP()
    configureSerialization()
}
