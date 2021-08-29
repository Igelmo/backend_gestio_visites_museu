package fib.museu.domain.repository

import fib.museu.domain.datamodels.RequestedBookingObject
import fib.museu.domain.datamodels.VisitObject
import java.time.LocalDateTime

interface BookingRepository {
    fun setNewBooking(requestedBookingObject: RequestedBookingObject)
    fun getRequestedBookings(): List<RequestedBookingObject>
    fun getRequestedBooking(dateTime: LocalDateTime): RequestedBookingObject
    fun removeRequestedBooking(dateTime: LocalDateTime)

    fun setNewVisit(visitObject: VisitObject)
    fun getVisits(): List<VisitObject>
    fun getVisit(dateTime: LocalDateTime): VisitObject
    fun getPendingVisits(): List<VisitObject>
    fun getCompletedVisits(): List<VisitObject>
    fun removeVisit(dateTime: LocalDateTime)
}