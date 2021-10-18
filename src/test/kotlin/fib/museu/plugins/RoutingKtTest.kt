package fib.museu.plugins

import fib.museu.domain.datamodels.*
import fib.museu.domain.repository.BookingRepository
import fib.museu.domain.repository.EmailRepository
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class RoutingKtTest {

    private val bookingRepository: BookingRepository = mockk()
    private val emailRepository: EmailRepository = mockk()
    private val person = PersonObject("fake@fake.fake", "name", "surname", "123456789")
    private val visitor = VisitorObject("fake@fake.fake", person, "center")
    private val booking = RequestedBookingObject(LocalDateTime.now(), visitor, 1, AssistantsType.Altre)
    private val visit = VisitObject(booking.requestedDateTime, booking, "fakeguide@fake.fake", false)

    @Test
    fun `basic routing uri is working`() {
        withTestApplication({ configureRouting(bookingRepository, emailRepository) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Backend is working!", response.content)
            }
        }
    }

    @Test
    fun `basic routing response uri is working`() {
        withTestApplication({ configureRouting(bookingRepository, emailRepository) }) {
            handleRequest(HttpMethod.Get, "/response").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Response from backend!", response.content)
            }
        }
    }

    @Test
    fun `requestedBookings should return an ok status if getRequestedBookings doesnt fail`() {
        every { bookingRepository.getRequestedBookings() } returns listOf(booking, booking)

        withTestApplication({
            configureSerialization()
            configureRouting(bookingRepository, emailRepository)
        }) {
            handleRequest(HttpMethod.Get, "/requestedBookings").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `requestedBookings should return an ok status if getRequestedBookings returns an empty list`() {
        every { bookingRepository.getRequestedBookings() } returns emptyList()

        withTestApplication({
            configureSerialization()
            configureRouting(bookingRepository, emailRepository)
        }) {
            handleRequest(HttpMethod.Get, "/requestedBookings").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }


    @Test
    fun `requestedBookings should return an error status if getRequestedBookings failed`() {
        every { bookingRepository.getRequestedBookings() } throws Exception("Test")

        withTestApplication({ configureRouting(bookingRepository, emailRepository) }) {
            handleRequest(HttpMethod.Get, "/requestedBookings").apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `requestedBookings should return an accepted status if removeRequestedBooking returns doesnt fail`() {
        every { bookingRepository.removeRequestedBooking(any()) } returns Unit
        every { bookingRepository.getRequestedBooking(any()) } returns booking
        every { emailRepository.sendEmail(booking, 1) } returns Unit

        val requestedDateTime = LocalDateTime.now().toString()
        withTestApplication({
            configureSerialization()
            configureRouting(bookingRepository, emailRepository)
        }) {
            with(handleRequest(HttpMethod.Delete, "/requestedBookings/$requestedDateTime")) {
                assertEquals(HttpStatusCode.Accepted, response.status())
            }
        }
    }

    @Test
    fun `requestedBookings should return an error status if removeRequestedBooking fails`() {
        every { bookingRepository.removeRequestedBooking(any()) } throws Exception("Test")
        every { bookingRepository.getRequestedBooking(any()) } returns booking
        every { emailRepository.sendEmail(booking, 1) } returns Unit

        val requestedDateTime = LocalDateTime.now().toString()
        withTestApplication({
            configureSerialization()
            configureRouting(bookingRepository, emailRepository)
        }) {
            handleRequest(HttpMethod.Delete, "/requestedBookings/$requestedDateTime").apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `bookings should return created if setNewBooking doesnt fail`() {
        every { bookingRepository.setNewBooking(any()) } returns Unit

        withTestApplication({
            configureSerialization()
            configureRouting(bookingRepository, emailRepository)
        }) {
            with(handleRequest(HttpMethod.Post, "/bookings") {
                setBodyRequestBookingObject(booking)
            }) {
                verify { bookingRepository.setNewBooking(booking) }
                assertEquals(HttpStatusCode.Created, response.status())
                assertEquals("Solicitud feta amb exit", response.content)
            }
        }
    }

    private fun TestApplicationRequest.setBodyRequestBookingObject(requestedBookingObject: RequestedBookingObject) {
        addHeader(HttpHeaders.Accept, ContentType.Text.Plain.contentType)
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(Json.encodeToString(requestedBookingObject))
    }

    @Test
    internal fun `if there is no body, bookings should return error`() {
        every { bookingRepository.setNewBooking(any()) } throws Exception("Test")

        withTestApplication({ configureRouting(bookingRepository, emailRepository) }) {
            handleRequest(HttpMethod.Post, "/bookings").apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("ERROR", response.content)
            }
        }
    }

    @Test
    fun `pendingVisits should return an ok status if getPendingVisits doesnt fail`() {
        every { bookingRepository.getPendingVisits() } returns listOf(visit, visit)

        withTestApplication({
            configureSerialization()
            configureRouting(bookingRepository, emailRepository)
        }) {
            handleRequest(HttpMethod.Get, "/pendingVisits").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `pendingVisits should return an ok status if getPendingVisits returns an empty list`() {
        every { bookingRepository.getPendingVisits() } returns emptyList()

        withTestApplication({
            configureSerialization()
            configureRouting(bookingRepository, emailRepository)
        }) {
            handleRequest(HttpMethod.Get, "/pendingVisits").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `visits should return created if setNewVisit doesnt fail`() {
        every { bookingRepository.setNewVisit(any()) } returns Unit
        every { emailRepository.sendEmail(visit.requestedBooking, 0) } returns Unit

        withTestApplication({
            configureSerialization()
            configureRouting(bookingRepository, emailRepository)
        }) {
            with(handleRequest(HttpMethod.Post, "/visits") {
                setBodyVisitObject(visit)
            }) {
                verify { bookingRepository.setNewVisit(visit) }
                assertEquals(HttpStatusCode.Created, response.status())
                assertEquals("Reserva acceptada correctament", response.content)
            }
        }
    }

    private fun TestApplicationRequest.setBodyVisitObject(visitObject: VisitObject) {
        addHeader(HttpHeaders.Accept, ContentType.Text.Plain.contentType)
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(Json.encodeToString(visitObject))
    }

    @Test
    fun `visits should return an accepted status if removeVisit returns doesnt fail`() {
        every { bookingRepository.removeVisit(any()) } returns Unit
        every { bookingRepository.getVisit(any()) } returns visit
        every { emailRepository.sendEmail(visit.requestedBooking, 2) } returns Unit

        val requestedDateTime = LocalDateTime.now().toString()
        withTestApplication({
            configureSerialization()
            configureRouting(bookingRepository, emailRepository)
        }) {
            with(handleRequest(HttpMethod.Delete, "/visits/$requestedDateTime")) {
                assertEquals(HttpStatusCode.Accepted, response.status())
            }
        }
    }

    @Test
    fun `visit should return an error status if removeVisit fails`() {
        every { bookingRepository.removeVisit(any()) } throws Exception("Test")
        every { bookingRepository.getVisit(any()) } returns visit
        every { emailRepository.sendEmail(visit.requestedBooking, 2) } returns Unit

        val requestedDateTime = LocalDateTime.now().toString()
        withTestApplication({
            configureSerialization()
            configureRouting(bookingRepository, emailRepository)
        }) {
            handleRequest(HttpMethod.Delete, "/visits/$requestedDateTime").apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

}