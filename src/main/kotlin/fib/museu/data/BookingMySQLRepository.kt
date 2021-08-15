package fib.museu.data

import fib.museu.domain.datamodels.RequestedBookingObject
import fib.museu.domain.datamodels.VisitObject
import fib.museu.domain.repository.BookingRepository
import fib.museu.domain.repository.PersonRepository
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.add
import java.time.LocalDateTime


class BookingMySQLRepository(
    private val ktormDatabase: Database,
    private val personRepository: PersonRepository

) : BookingRepository {

    override fun setNewBooking(requestedBookingObject: RequestedBookingObject) {
        val requestedBooking = RequestedBooking(requestedBookingObject)
        runCatching {
            personRepository.setVisitor(requestedBookingObject.visitor)
        }

        ktormDatabase.bookings.add(requestedBooking)


    }

    override fun getRequestedBookings(): List<RequestedBookingObject> = ktormDatabase.from(RequestedBookings).select().map { it.asBooking() }

    override fun getRequestedBooking(dateTime: LocalDateTime): RequestedBookingObject = ktormDatabase.from(RequestedBookings).select()
        .where { RequestedBookings.requestedDateTime eq dateTime }
        .limit(0, 1)
        .map { it.asBooking() }
        .first()

    override fun getVisits(): List<VisitObject> = ktormDatabase.from(Visits).select().map { it.asVisit() }

    override fun getPendingVisits(): List<VisitObject> = getVisits().filter { !it.completed }

    override fun getCompletedVisits(): List<VisitObject> = getVisits().filter { it.completed }

    override fun removeRequestedBooking(dateTime: LocalDateTime) {
        ktormDatabase.from(RequestedBookings).select().where { RequestedBookings.requestedDateTime eq dateTime }.map { it.asBooking() }.forEach { println("${it.requestedDateTime}") }
        ktormDatabase.from(RequestedBookings).select().where { RequestedBookings.requestedDateTime eq dateTime.minusDays(1) }.map { it.asBooking() }.forEach { println("${it.requestedDateTime}") }
        ktormDatabase.from(RequestedBookings).select().where { RequestedBookings.requestedDateTime eq dateTime.plusDays(1) }.map { it.asBooking() }.forEach { println("${it.requestedDateTime}") }

        ktormDatabase.delete(RequestedBookings) {
            it.requestedDateTime eq dateTime
        }
    }

    private fun QueryRowSet.asBooking(): RequestedBookingObject {
        val contactEmail = get(RequestedBookings.contactEmail) ?: throw IllegalStateException("contactEmail has to be not null")

        val visitor = personRepository.getVisitor(contactEmail)

        return RequestedBookingObject(
            requestedDateTime = get(RequestedBookings.requestedDateTime) ?: throw IllegalStateException("Missing Primary key day"),
            visitor = visitor,
            assistants = get(RequestedBookings.assistants) ?: throw IllegalStateException("Assistants has to be not null"),
            assistantsType = get(RequestedBookings.typeAssistant) ?: throw IllegalStateException("TypeAssistant has to be not null"),
            comments = get(RequestedBookings.comments),
            accepted = get(RequestedBookings.accepted) ?: false,
        )
    }

    private fun QueryRowSet.asVisit(): VisitObject {
        val dateTime = get(Visits.visitDateTime) ?: throw IllegalStateException("visitDay has to be not null")

        val requestedBooking = getRequestedBooking(dateTime)

        return VisitObject(
            visitDateTime = dateTime,
            requestedBookingObject = requestedBooking,
            guideEmail = get(Visits.guideEmail) ?: throw IllegalStateException("Has to be a guide assigned"),
            completed = get(Visits.completed) ?: false,
        )
    }
}