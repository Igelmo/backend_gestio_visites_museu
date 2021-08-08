package fib.museu.plugins

import fib.museu.data.BookingMySQLRepository
import fib.museu.data.PersonMySQLRepository
import fib.museu.domain.datamodels.RequestedBookingObject
import fib.museu.domain.repository.BookingRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import org.ktorm.database.Database

private val username = "dummy" // provide the username
private val password = "dummy" // provide the corresponding password
private val ktormDatabase = Database.connect(
    "jdbc:mysql://localhost:3306/mydb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
    user = username,
    password = password
)


fun Application.configureRouting() {
    val repository: BookingRepository = BookingMySQLRepository(ktormDatabase, PersonMySQLRepository(ktormDatabase))

    install(Locations) {
    }

    routing {
        get("/") {
            call.respondText { "Backend is working!" }
        }
        get("/response") {
            call.respondText("Response from backend!")
        }
        post("/bookings") {
            runCatching {
                val booking = call.receive<RequestedBookingObject>()
                repository.setNewBooking(booking)
                call.respondText("Reserva feta correctament", status = HttpStatusCode.Created)
            }.onFailure {
                log.error(it)
                call.respondText("ERROR", status = HttpStatusCode.InternalServerError)
            }
        }
        get<MyLocation> {
            call.respondText("Location: name=${it.name}, arg1=${it.arg1}, arg2=${it.arg2}")
        }
        // Register nested routes
        get<Type.Edit> {
            call.respondText("Inside $it")
        }
        get<Type.List> {
            call.respondText("Inside $it")
        }
    }
}

@Location("/location/{name}")
class MyLocation(val name: String, val arg1: Int = 42, val arg2: String = "default")

@Location("/type/{name}")
data class Type(val name: String) {
    @Location("/edit")
    data class Edit(val type: Type)

    @Location("/list/{page}")
    data class List(val type: Type, val page: Int)
}
