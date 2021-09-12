package fib.museu.domain.datamodels

import kotlinx.serialization.Serializable

@Serializable
data class VisitorObject(
    val visitorEmail: String,
    val person: PersonObject,
    val center: String,
)
