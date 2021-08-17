package fib.museu.domain.repository

import fib.museu.domain.datamodels.RequestedBookingObject
import fib.museu.domain.datamodels.VisitObject
import java.time.LocalDateTime

interface BookingRepository {
    fun setNewBooking(requestedBookingObject: RequestedBookingObject)
    fun setNewVisit(visitObject: VisitObject)
    fun getRequestedBookings(): List<RequestedBookingObject>
    fun getRequestedBooking(dateTime: LocalDateTime): RequestedBookingObject
    fun getVisits(): List<VisitObject>
    fun getPendingVisits(): List<VisitObject>
    fun getCompletedVisits(): List<VisitObject>
    fun removeRequestedBooking(dateTime: LocalDateTime)
}