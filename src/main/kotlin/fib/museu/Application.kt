package fib.museu

import fib.museu.di.mainModule
import fib.museu.domain.repository.BookingRepository
import fib.museu.plugins.configureMonitoring
import fib.museu.plugins.configureRouting
import fib.museu.plugins.configureSerialization
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.fileProperties
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.SLF4JLogger

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080) {
        install(Koin) {
            SLF4JLogger()
            fileProperties()
            modules(mainModule)
        }
        install(CORS) {
            allowNonSimpleContentTypes = true
            header(HttpHeaders.AccessControlAllowHeaders)
            header(HttpHeaders.AccessControlAllowOrigin)
            header(HttpHeaders.ContentType)
            allowCredentials = true
            anyHost()
            method(HttpMethod.Delete)
        }

        val bookingRepository by inject<BookingRepository>()
        configureRouting(bookingRepository)
        configureMonitoring()
        configureSerialization()
        environment.config
    }.start(wait = true)
}
