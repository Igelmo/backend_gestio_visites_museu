package fib.museu.plugins

import fib.museu.domain.datamodels.AssistantsType
import fib.museu.domain.datamodels.PersonObject
import fib.museu.domain.datamodels.RequestedBookingObject
import fib.museu.domain.datamodels.VisitorObject
import fib.museu.domain.repository.BookingRepository
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
    private val person = PersonObject("fake@fake.fake", "name", "surname", "123456789")
    private val visitor = VisitorObject("fake@fake.fake", person, "center")
    private val booking = RequestedBookingObject(LocalDateTime.now(), visitor, 1, AssistantsType.Altre)

    @Test
    fun `basic routing uri is working` () {
        withTestApplication({ configureRouting(bookingRepository) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Backend is working!", response.content)
            }
        }
    }

    @Test
    fun `basic routing response uri is working`() {
        withTestApplication({ configureRouting(bookingRepository) }) {
            handleRequest(HttpMethod.Get, "/response").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Response from backend!", response.content)
            }
        }
    }

    @Test
    fun `requestedBookings should return an ok status if gerRequestedBookings doesnt fail`() {
        every { bookingRepository.getRequestedBookings() } returns listOf(booking, booking)

        withTestApplication({
            configureSerialization()
            configureRouting(bookingRepository)
        }) {
            handleRequest(HttpMethod.Get, "/requestedBookings").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `requestedBookings should return an ok status if gerRequestedBookings returns an empty list`() {
        every { bookingRepository.getRequestedBookings() } returns emptyList()

        withTestApplication({
            configureSerialization()
            configureRouting(bookingRepository)
        }) {
            handleRequest(HttpMethod.Get, "/requestedBookings").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }


    @Test
    fun `requestedBookings should return an error status if gerRequestedBookings failed`() {
        every { bookingRepository.getRequestedBookings() } throws Exception("Test")

        withTestApplication({ configureRouting(bookingRepository) }) {
            handleRequest(HttpMethod.Get, "/requestedBookings").apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
            }
        }
    }

    @Test
    fun `bookings should return created if setNewBooking doesnt fail`() {
        every { bookingRepository.setNewBooking(any()) } returns Unit

        withTestApplication({
            configureSerialization()
            configureRouting(bookingRepository)
        }) {
            with(handleRequest(HttpMethod.Post, "/bookings") {
                setBodyObject(booking)
            }) {
                verify { bookingRepository.setNewBooking(booking) }
                assertEquals(HttpStatusCode.Created, response.status())
                assertEquals("Solicitud feta amb exit", response.content)
            }
        }
    }

    private fun TestApplicationRequest.setBodyObject(requestedBookingObject: RequestedBookingObject) {
        addHeader(HttpHeaders.Accept, ContentType.Text.Plain.contentType)
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(Json.encodeToString(requestedBookingObject))
    }

    @Test
    internal fun `if there is no body, bookings should return error`() {
        every { bookingRepository.setNewBooking(any()) } throws Exception("Test")

        withTestApplication({ configureRouting(bookingRepository) }) {
            handleRequest(HttpMethod.Post, "/bookings").apply {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("ERROR", response.content)
            }
        }

    }
}