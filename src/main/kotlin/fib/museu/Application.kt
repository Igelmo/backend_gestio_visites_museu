package fib.museu

import fib.museu.plugins.MySQLDatabaseExampleKotlin
import fib.museu.plugins.configureMonitoring
import fib.museu.plugins.configureRouting
import fib.museu.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureMonitoring()
        configureSerialization()
        MySQLDatabaseExampleKotlin.getConnection()
    }.start(wait = true)
}