package fib.museu.domain.datamodels

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class RequestedBookingObject(
    @Serializable(with = LocalDateTimeSerializer::class) val requestedDateTime: LocalDateTime,
    val visitor: VisitorObject,
    val assistants: Int,
    val assistantsType: AssistantsType,
    val comments: String? = null,
    val accepted: Boolean = false,
)

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDateTime) = encoder.encodeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    override fun deserialize(decoder: Decoder): LocalDateTime =
        LocalDateTime.parse(decoder.decodeString().replace("Z", ""), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}