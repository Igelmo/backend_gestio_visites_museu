package fib.museu.domain.datamodels

import kotlinx.serialization.Serializable

@Serializable
data class VisitorObject(
    val email: String,
    val name: String,
    val surname: String,
    val phone: String,
    val center: String,
)
