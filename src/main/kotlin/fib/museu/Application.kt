package fib.museu

import fib.museu.plugins.configureMonitoring
import fib.museu.plugins.configureRouting
import fib.museu.plugins.configureSerialization
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(CORS) {
            allowNonSimpleContentTypes = true
            anyHost()
            method(HttpMethod.Delete)
        }
        configureRouting()
        configureMonitoring()
        configureSerialization()
    }.start(wait = true)
}
