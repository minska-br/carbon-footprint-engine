package br.com.footprint.carbon

import br.com.footprint.carbon.configurations.plugins.configureHTTP
import br.com.footprint.carbon.configurations.plugins.configureKoin
import br.com.footprint.carbon.configurations.plugins.configureRouting
import br.com.footprint.carbon.configurations.plugins.configureSerialization
import br.com.footprint.carbon.configurations.plugins.configureSqs
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
    configureKoin()
    configureSqs()
}
