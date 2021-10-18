package fib.museu.domain.repository

import fib.museu.domain.datamodels.RequestedBookingObject

interface EmailRepository {
    fun sendEmail(requestedBooking: RequestedBookingObject, type: Int)
}