package fib.museu.domain.datamodels

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Serializable
data class RequestedBookingObject(
    @Serializable(with = LocalDateSerializer::class) val requestedDay: LocalDate,
    @Serializable(with = LocalTimeSerializer::class) val requestedHour: LocalTime,
    val visitor: VisitorObject,
    val assistants: Int,
    val assistantsType: AssistantsType,
    val comments: String? = null,
    val accepted: Boolean = false,
)

object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDate) = encoder.encodeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE))
    override fun deserialize(decoder: Decoder): LocalDate =
        LocalDate.parse(decoder.decodeString().split("T")[0], DateTimeFormatter.ISO_LOCAL_DATE)
}

object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalTime) = encoder.encodeString(value.format(DateTimeFormatter.ISO_LOCAL_TIME))
    override fun deserialize(decoder: Decoder): LocalTime =
        LocalTime.parse(decoder.decodeString().split("T")[1].replace("Z", ""), DateTimeFormatter.ISO_LOCAL_TIME)
}