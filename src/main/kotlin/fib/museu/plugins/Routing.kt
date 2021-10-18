package fib.museu.plugins

import fib.museu.domain.datamodels.EmailSender
import fib.museu.domain.datamodels.RequestedBookingObject
import fib.museu.domain.datamodels.VisitObject
import fib.museu.domain.repository.BookingRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import org.koin.ktor.ext.inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Application.configureRouting(bookingRepository: BookingRepository) {
    val email by inject<EmailSender>()

    install(Locations) {
    }

    routing {

        get("/") {
            call.respondText { "Backend is working!" }
        }

        get("/response") {
            call.respondText("Response from backend!")
        }

        get("/requestedBookings") {
            runCatching {
                call.respond(bookingRepository.getRequestedBookings())
            }.onFailure {
                log.error(it)
                call.respondText("ERROR", status = HttpStatusCode.InternalServerError)
            }
        }

        get("/pendingVisits") {
            runCatching {
                call.respond(bookingRepository.getPendingVisits())
            }.onFailure {
                log.error(it)
                call.respondText("ERROR", status = HttpStatusCode.InternalServerError)
            }
        }

        delete("/requestedBookings/{requestedDateTime}") {
            runCatching {
                val dateTime = LocalDateTime.parse(call.parameters["requestedDateTime"], DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                val requestedBooking = bookingRepository.getRequestedBooking(dateTime)
                bookingRepository.removeRequestedBooking(dateTime)
                call.respond(HttpStatusCode.Accepted)
                email.sendEmail(requestedBooking, 1)
            }.onFailure {
                log.error(it)
                call.respondText("ERROR", status = HttpStatusCode.InternalServerError)
            }
        }

        delete("/visits/{requestedDateTime}") {
            runCatching {
                val dateTime = LocalDateTime.parse(call.parameters["requestedDateTime"], DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                val visit = bookingRepository.getVisit(dateTime)
                bookingRepository.removeVisit(dateTime)
                call.respond(HttpStatusCode.Accepted)
                email.sendEmail(visit.requestedBooking, 2)
            }.onFailure {
                log.error(it)
                call.respondText("ERROR", status = HttpStatusCode.InternalServerError)
            }
        }

        post("/bookings") {
            runCatching {
                val booking = call.receive<RequestedBookingObject>()
                bookingRepository.setNewBooking(booking)
                call.respondText("Solicitud feta amb exit", status = HttpStatusCode.Created)
            }.onFailure {
                log.error(it)
                call.respondText("ERROR", status = HttpStatusCode.InternalServerError)
            }
        }

        post("/visits") {
            runCatching {
                val visit = call.receive<VisitObject>()
                bookingRepository.setNewVisit(visit)
                call.respondText("Reserva acceptada correctament", status = HttpStatusCode.Created)
                email.sendEmail(visit.requestedBooking, 0)
            }.onFailure {
                log.error(it)
                call.respondText("ERROR", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}