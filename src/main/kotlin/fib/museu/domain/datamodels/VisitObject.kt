package fib.museu.domain.datamodels

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class VisitObject(
    @Contextual val visitDay: LocalDate,
    @Contextual val visitHour: LocalTime,
    val requestedBookingObject: RequestedBookingObject,
    val guideEmail: String,
    val completed: Boolean,
)