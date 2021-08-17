package fib.museu.domain.datamodels
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class VisitObject(
    @Serializable(with = LocalDateTimeSerializer::class) val visitDateTime: LocalDateTime,
    val requestedBooking: RequestedBookingObject,
    val guideEmail: String,
    val completed: Boolean,
)