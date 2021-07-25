package fib.museu.domain.repository

import fib.museu.domain.datamodels.Booking

interface BookingRepository {
    fun setNewBooking(booking: Booking)
    fun getRequestedBookings(): List<Booking>
}