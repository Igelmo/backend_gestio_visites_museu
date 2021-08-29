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

    override fun setNewVisit(visitObject: VisitObject) {
        val visit = Visit(visitObject)
        ktormDatabase.visits.add(visit)
        ktormDatabase.update(RequestedBookings) {
            set(it.accepted, true)
            where { it.requestedDateTime eq visitObject.visitDateTime }
        }
    }

    override fun getRequestedBookings(): List<RequestedBookingObject> = ktormDatabase.from(RequestedBookings).select()
        .where { !RequestedBookings.accepted }
        .map { it.asBooking() }

    override fun getRequestedBooking(dateTime: LocalDateTime): RequestedBookingObject = ktormDatabase.from(RequestedBookings).select()
        .where { RequestedBookings.requestedDateTime eq dateTime }
        .limit(0, 1)
        .map { it.asBooking() }
        .first()

    override fun getVisits(): List<VisitObject> = ktormDatabase.from(Visits).select().map { it.asVisit() }

    override fun getVisit(dateTime: LocalDateTime): VisitObject = ktormDatabase.from(Visits).select()
        .where { Visits.visitDateTime eq dateTime }
        .limit(0, 1)
        .map { it.asVisit() }
        .first()

    override fun getPendingVisits(): List<VisitObject> = getVisits().filter { !it.completed }

    override fun getCompletedVisits(): List<VisitObject> = getVisits().filter { it.completed }

    override fun removeRequestedBooking(dateTime: LocalDateTime) {
        ktormDatabase.delete(RequestedBookings) {
            it.requestedDateTime eq dateTime
        }
    }

    override fun removeVisit(dateTime: LocalDateTime) {
        ktormDatabase.delete(Visits) {
            it.visitDateTime eq dateTime
        }
        removeRequestedBooking(dateTime)
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
            requestedBooking = requestedBooking,
            guideEmail = get(Visits.guideEmail) ?: throw IllegalStateException("Has to be a guide assigned"),
            completed = get(Visits.completed) ?: false,
        )
    }
}