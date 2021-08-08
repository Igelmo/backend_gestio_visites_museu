package fib.museu.data

import fib.museu.domain.datamodels.RequestedBookingObject
import fib.museu.domain.datamodels.VisitObject
import fib.museu.domain.repository.BookingRepository
import fib.museu.domain.repository.PersonRepository
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.add
import java.time.LocalDate
import java.time.LocalTime


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

    override fun getRequestedBooking(day: LocalDate, hour: LocalTime): RequestedBookingObject = ktormDatabase.from(RequestedBookings).select()
        .where { (RequestedBookings.requestedDay eq day) and (RequestedBookings.requestedHour eq hour) }
        .limit(0, 1)
        .map { it.asBooking() }
        .first()

    override fun getVisits(): List<VisitObject> = ktormDatabase.from(Visits).select().map { it.asVisit() }

    override fun getPendingVisits(): List<VisitObject> = getVisits().filter { !it.completed }

    override fun getCompletedVisits(): List<VisitObject> = getVisits().filter { it.completed }

    private fun QueryRowSet.asBooking(): RequestedBookingObject {
        val contactEmail = get(RequestedBookings.contactEmail) ?: throw IllegalStateException("contactEmail has to be not null")

        val visitor = personRepository.getVisitor(contactEmail)

        return RequestedBookingObject(
            requestedDay = get(RequestedBookings.requestedDay) ?: throw IllegalStateException("Missing Primary key day"),
            requestedHour = get(RequestedBookings.requestedHour) ?: throw IllegalStateException("Missing Primary key hour"),
            visitor = visitor,
            assistants = get(RequestedBookings.assistants) ?: throw IllegalStateException("Assistants has to be not null"),
            assistantsType = get(RequestedBookings.typeAssistant) ?: throw IllegalStateException("TypeAssistant has to be not null"),
            comments = get(RequestedBookings.comments),
            accepted = get(RequestedBookings.accepted) ?: false,
        )
    }

    private fun QueryRowSet.asVisit(): VisitObject {
        val day = get(Visits.visitDay) ?: throw IllegalStateException("visitDay has to be not null")
        val hour = get(Visits.visitHour) ?: throw IllegalStateException("visitHour has to be not null")

        val requestedBooking = getRequestedBooking(day, hour)

        return fib.museu.domain.datamodels.VisitObject(
            visitDay = day,
            visitHour = hour,
            requestedBookingObject = requestedBooking,
            guideEmail = get(Visits.guideEmail) ?: throw IllegalStateException("Has to be a guide assigned"),
            completed = get(Visits.completed) ?: false,
        )
    }
}