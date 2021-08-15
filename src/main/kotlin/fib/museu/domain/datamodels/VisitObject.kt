package fib.museu.domain.datamodels

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class VisitObject(
    @Contextual val visitDateTime: LocalDateTime,
    val requestedBookingObject: RequestedBookingObject,
    val guideEmail: String,
    val completed: Boolean,
)