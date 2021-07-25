package fib.museu.domain.datamodels

import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val name: String,
    val surname: String,
    val center: String,
    val email: String,
    val phone: String,
    val assistants: Int,
    val assistantsType: AssistantsType,
    val comments: String?
)
