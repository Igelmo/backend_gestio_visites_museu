package fib.museu.data

import fib.museu.domain.datamodels.Booking
import fib.museu.domain.repository.BookingRepository

class BookingMySQLRepository : BookingRepository {
    override fun setNewBooking(booking: Booking) {

    }

    override fun getRequestedBookings(): List<Booking> {
        TODO("Not yet implemented")
    }
}