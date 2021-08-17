package fib.museu.data

import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf


val Database.bookings get() = this.sequenceOf(RequestedBookings)
val Database.visits get() = this.sequenceOf(Visits)
val Database.visitors get() = this.sequenceOf(Visitors)