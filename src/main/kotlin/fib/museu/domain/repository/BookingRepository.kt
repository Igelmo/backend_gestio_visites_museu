package fib.museu.domain.repository

import fib.museu.domain.datamodels.RequestedBookingObject
import fib.museu.domain.datamodels.VisitObject
import java.time.LocalDate
import java.time.LocalTime

interface BookingRepository {
    fun setNewBooking(requestedBookingObject: RequestedBookingObject)
    fun getRequestedBookings(): List<RequestedBookingObject>
    fun getRequestedBooking(day: LocalDate, hour: LocalTime): RequestedBookingObject
    fun getVisits(): List<VisitObject>
    fun getPendingVisits(): List<VisitObject>
    fun getCompletedVisits(): List<VisitObject>
}